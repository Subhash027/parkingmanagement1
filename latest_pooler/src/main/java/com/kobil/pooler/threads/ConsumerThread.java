package com.kobil.pooler.threads;


import com.kobil.core.exceptions.KobilSoapException;
import com.kobil.core.exceptions.PollAllDeviceEventsNotFoundException;
import com.kobil.core.kakfa.KafkaBrokerProducer;
import com.kobil.core.soap.asm.svc.functions.GetPropertyFunction;
import com.kobil.core.soap.asm.svc.functions.PollDeviceEventsFunction;
import com.kobil.core.soap.asm.svc.io.ErrorCodes;
import com.kobil.core.soap.models.DeviceEvent;
import com.kobil.core.soap.models.DeviceEvents;
import com.kobil.core.soap.models.PollModel;
import com.kobil.core.soap.models.PropertyType;
import com.kobil.core.soap.responses.SoapGetPropertyResponse;
import com.kobil.core.soap.responses.SoapPollDeviceEventsResponse;
import com.kobil.core.utils.Constants;
import com.kobil.core.utils.LogHandler;
import com.kobil.core.utils.ObjectHandler;
import com.kobil.core.utils.UrlHandler;
import com.kobil.pooler.listeners.ConsumerListener;
import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ConsumerThread implements Runnable {

  private LinkedBlockingQueue<PollModel> _queue;
  List<DeviceEvents> _events;
  KafkaBrokerProducer _producer;
  String _authorization;
  String _tenant;
  ConsumerListener _consumerListener;
  String _poolKey;

  public ConsumerThread(String poolKey, String tenant, LinkedBlockingQueue<PollModel> queue,
      String authorization, ConsumerListener consumerListener) {
    if(tenant != null) {
      LogHandler.getInstance().log("Initiated Thread for Tenant: " + tenant);
    }
    else
    {
      LogHandler.getInstance().log("Initiated Thread");
    }
    this._tenant = tenant;
    this._queue = queue;
    this._poolKey = poolKey;
    this._producer = KafkaBrokerProducer.getInstance();
    this._authorization = authorization;
    this._consumerListener = consumerListener;
    _events = new ArrayList<>();
    _events.add(DeviceEvents.TRANSACTION_RESULT_READY);
    _events.add(DeviceEvents.DEVICE_ACTIVATION);
    _events.add(DeviceEvents.SET_PROPERTY);
    _events.add(DeviceEvents.DEVICE_ON);
  }

  @Override
  public void run() {
    while (true) {

      if (_queue.size() != 0) {
        try {
          PollDeviceEventsFunction pollEvents = new PollDeviceEventsFunction(this._authorization);
          SoapPollDeviceEventsResponse response = pollEvents.execute();
          if(response != null && response.getReturn() != null && response.getReturn().getDeviceEvents() != null){
            System.out.println("entering");
            HashMap<String, String> map = new HashMap<>();
            try {
              map.put("Status", response.getReturn().getReturnStatus());
              map.put("Device event", response.getReturn().getDeviceEvents().toString());
              map.put("Node id", response.getReturn().getNodeID());
            }catch (Exception e){
            }
            PollModel pollModel = new PollModel();
            pollModel.setAdditionalArgs(map);
            pollModel.setListenerKey("1234567890");
            pollModel.setDeviceEvent(DeviceEvents.DEVICE_SEND_SMS);
            _producer.send( UrlHandler.getInstance().getKafkaBrokerCommonResponseTopic(), pollModel);
          }
          if (response != null && response.getReturn() != null && response.getReturn().getDeviceEvents() != null && response.getReturn().getDeviceEvents().size() > 0) {
            List<DeviceEvent> filteredEvents = response.getReturn().getDeviceEvents().stream()
                .filter(x -> (_events.contains(x.getDeviceEvents()))).collect(Collectors.toList());
            if (filteredEvents.size() > 0) {
              processResult(filteredEvents);
            }
            else
            {
              LogHandler.getInstance().log("No Events to process");
            }
          } else {
            LogHandler.getInstance().log("No Events to process");
          }
        } catch (KobilSoapException e) {
          if (e.getErrorCode() == String.valueOf(ErrorCodes.NO_DEVICE_EVENTS)) {
            LogHandler.getInstance().log("No Events to process");
          }
        }
        if (UrlHandler.getInstance().getPollDelay() > 0) {
          try {
            TimeUnit.MILLISECONDS.sleep(UrlHandler.getInstance().getPollDelay());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } else {
        LogHandler.getInstance().log("Queue is empty, Exiting the Thread!");
        break;
      }
    }

    _consumerListener.onCompleted(_poolKey);
  }


  private void processResult(List<DeviceEvent> events) {
    for (DeviceEvent event : events) {
      if (event.getDeviceEvents() == DeviceEvents.TRANSACTION_RESULT_READY) {
        processTransaction(event);
      } else if (event.getDeviceEvents() == DeviceEvents.DEVICE_ACTIVATION) {
        processActivation(event);
      } else if (event.getDeviceEvents() == DeviceEvents.SET_PROPERTY) {
        processSetProperty(event);
      }
    }
  }

  private void processSetProperty(DeviceEvent event) {
    LogHandler.getInstance()
        .log("Received property: " + ObjectHandler.getInstance().getGson().toJson(event));
    PollModel pollModel = null;
    List<PollModel> setPropertyModels = _queue.stream()
        .filter(x -> x.getDeviceEvent() == DeviceEvents.SET_PROPERTY)
        .collect(Collectors.toList());
    for (PollModel model : setPropertyModels) {
      pollModel = new PollModel();
      String[] getproperty = event.getEventInformation().split(":");
      String authorization = model.getAdditionalArgs().get(Constants.AUTHORIZATION);

      GetPropertyFunction getPropertyFunction = new GetPropertyFunction(
          String.valueOf(event.getDeviceID()), getproperty[1], PropertyType.DEVICE, authorization);
      SoapGetPropertyResponse getPropertyResponse = null;
      try {
        getPropertyResponse = getPropertyFunction.execute();
        byte[] value = getPropertyResponse.getReturn().getBinaryValue();
        String encode = new String(value);
        if (model.getEntity().equals(encode)) {
          this._queue.removeIf(x -> x.getEntity() != null && x.getEntity().equals(encode));
          pollModel.setListenerKey(model.getListenerKey());
          pollModel.setDeviceId(String.valueOf(event.getDeviceID()));
          pollModel.setUserId(event.getUserID());
          pollModel.setDeviceEvent(DeviceEvents.SET_PROPERTY);
          HashMap<String, String> getProperty = pollModel.getAdditionalArgs();
          if(getProperty == null)
          {
            getProperty= new HashMap<>();
          }
          getProperty.put(Constants.LOGIN_NOUNCE,encode);
          getProperty.put(Constants.GET_PROPERTY_FAILED, String.valueOf(true));
          pollModel.setAdditionalArgs(getProperty);
          pollModel.setReplyConsumerTopic(model.getReplyConsumerTopic());
        }
      } catch (Exception e) {
        HashMap<String, String> getProperty = new HashMap<>();
        if (e instanceof SOAPExceptionImpl) {
          getProperty.put(Constants.GET_PROPERTY_FAILED, "Unauthorized");
        } else {
          getProperty.put(Constants.GET_PROPERTY_FAILED, String.valueOf(false));
        }
        pollModel.setDeviceEvent(DeviceEvents.SET_PROPERTY);
        pollModel.setListenerKey(model.getListenerKey());
        pollModel.setAdditionalArgs(getProperty);
        pollModel.setReplyConsumerTopic(model.getReplyConsumerTopic());
        e.printStackTrace();
      }
    }
    if (pollModel != null) {
      _producer.send(pollModel.getReplyConsumerTopic(), pollModel);
    }
  }

  private void processActivation(DeviceEvent event) {
    for (PollModel model : _queue) {
      if (model != null && model.getUserId() != null && model.getUserId().equals(event.getUserID())
          && model.getDeviceEvent().equals(event.getDeviceEvents())) {
        this._queue.removeIf(x -> x.getUserId().equals(event.getUserID()));
        LogHandler.getInstance().log("Received Device Activation for userId: " + event.getUserID());
        PollModel pollModel = new PollModel();
        pollModel.setDeviceId(String.valueOf(event.getDeviceID()));
        pollModel.setUserId(event.getUserID());
        pollModel.setDeviceEvent(DeviceEvents.DEVICE_ACTIVATION);
        pollModel.setReplyConsumerTopic(model.getReplyConsumerTopic());
        _producer.send(pollModel.getReplyConsumerTopic(), pollModel);
      }
    }
  }

  private void processTransaction(DeviceEvent event) {
    List<PollModel> transactionModels = _queue.stream()
        .filter(x -> x.getDeviceEvent() == DeviceEvents.TRANSACTION_RESULT_READY)
        .collect(Collectors.toList());
    for (PollModel model : transactionModels) {
      if (event.getEventInformation().equals(model.getTransactionId())
          && event.getUserID().equals(model.getUserId())) {
        model.setDeviceId(String.valueOf(event.getDeviceID()));
        LogHandler.getInstance().log(
            "Received Transaction for userId: " + event.getUserID() + " for transactionId: "
                + event.getEventInformation());
        this._queue.removeIf(x -> x.getUserId() != null && x.getUserId().equals(event.getUserID())
            && x.getTransactionId().equals(event.getEventInformation()));
        _producer.send(model.getReplyConsumerTopic(), model);
      }
    }
  }
}
