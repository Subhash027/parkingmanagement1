package com.kobil.pooler;

import com.kobil.core.exceptions.KobilSoapException;
import com.kobil.core.kakfa.KafkaBrokerConsumer;
import com.kobil.core.kakfa.KafkaBrokerProducer;
import com.kobil.core.kakfa.KafkaConsumerListener;
import com.kobil.core.main.KobilCore;
import com.kobil.core.soap.asm.svc.functions.PollDeviceEventsFunction;
import com.kobil.core.soap.kernal.mgmt.functions.GetTenantsFunction;
import com.kobil.core.soap.kernal.mgmt.io.GetTenantsResponse;
import com.kobil.core.soap.models.PollModel;
import com.kobil.core.soap.responses.SoapPollDeviceEventsResponse;
import com.kobil.core.utils.LogHandler;
import com.kobil.core.utils.PollQueue;
import com.kobil.core.utils.PollQueueManager;
import com.kobil.core.utils.UrlHandler;
import com.kobil.pooler.listeners.ConsumerListener;
import com.kobil.pooler.threads.ConsumerThread;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;
import java.util.Arrays;

public class MainApplication implements CommandLineRunner {

  public static void main(String args[]) {
    KobilCore.getInstance().initialize(args[0]);
    SpringApplication.run(MainApplication.class, args);
  }

  public void run(String... args) throws Exception {
    LogHandler.getInstance().logInfo("Kobil Pooler Started");
    ExecutorService executorService = Executors
        .newFixedThreadPool(UrlHandler.getInstance().getPoolSize());
    LogHandler.getInstance().logInfo(
        "Completed Initiating Thread Pool for : " + UrlHandler.getInstance().getPoolSize());
    ArrayList topics = new ArrayList();
    String requestTopics = UrlHandler.getInstance().getKafkaBrokerTopic();
    String[] array = requestTopics.split(",");
    topics.addAll(Arrays.asList(array));
    LogHandler.getInstance().logInfo("Kafka Consumer Initiated with :" + requestTopics);
    KafkaBrokerConsumer<PollModel> kafkaConsumer = new KafkaBrokerConsumer<>(topics,"kafka-pooler-group",
        PollModel.class, new KafkaConsumerListener<PollModel>() {
      @Override
      public void onReceived(PollModel pollModel) {
        String authorization = pollModel.getAdditionalArgs().get("Authorization");
        if(authorization == null)
        {
          String login = UrlHandler.getInstance().getSvcUserName() + ":" + UrlHandler.getInstance().getSvcPassword();
          authorization = Base64.getEncoder().encodeToString(login.getBytes());
        }
        String key = pollModel.getTenantId() != null ? pollModel.getTenantId() + authorization: authorization;

        PollQueue pollQueue = PollQueueManager.getInstance().getQueue(key);
        LogHandler.getInstance().logInfo("Requested Tenant: " + pollModel.getTenantId());
        LogHandler.getInstance().logInfo("Requested Key: " + key);
        //First time the queue will not intialized, so we considered as fresh start and submit to executor service
        if (pollQueue == null) {
          pollQueue = PollQueueManager.getInstance().createQueue(key);
          pollQueue.getQueue().add(pollModel);
          LogHandler.getInstance().logInfo("New Queue Inialized");
          executorService
              .submit(new ConsumerThread(key, pollModel.getTenantId(), pollQueue.getQueue(),
                  authorization, new ConsumerListener() {
                @Override
                public void onCompleted(String poolKey) {
                  LogHandler.getInstance().logInfo("Removed Queue :" + poolKey);
                  PollQueueManager.getInstance().removeQueue(poolKey);
                }
              }));
        } else {
          LogHandler.getInstance().logInfo("Added to exisiting Queue");
          //Next time we just add to the queue
          pollQueue.getQueue().add(pollModel);
        }
      }
    });
    kafkaConsumer.start();

  }
}
