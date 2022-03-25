package com.grootan.parkingmanagement.exception;

import com.grootan.parkingmanagement.domain.ResponeDetails;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message= ex.getMessage();
        String details="MISMATCH OF TYPE";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details,status);
        return ResponseEntity.status(status).body(responeDetails);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message= ex.getMessage();
        String details="REQUEST PARAM IS MISSING";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details,status);
        return ResponseEntity.status(status).body(responeDetails);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message= ex.getMessage();
        String details="PATH VARIABLE MISSING";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details,status);
        return ResponseEntity.status(status).body(responeDetails);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message= ex.getMessage();
        String details="MESSAGE NOT READABLE";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details,status);
        return ResponseEntity.status(status).body(responeDetails);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message= ex.getMessage();
        String details="METHOD NOT SUPPORTED";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details,status);
        return ResponseEntity.status(status).body(responeDetails);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message= ex.getMessage();
        String details="METHOD NOT SUPPORTED";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details,status);
        return ResponseEntity.status(status).body(responeDetails);
    }
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Object> handleNoContantException(NoContentException exception)
    {
        String message=HttpStatus.NO_CONTENT+exception.getMessage();
        String details="there is no contant in the file!";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details, HttpStatus.NOT_ACCEPTABLE);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responeDetails);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleActorNotFoundException(ResourceNotFoundException resourceNotFoundException)
    {
        String message=resourceNotFoundException.getMessage();
        String details="resource not found ";
        ResponeDetails responeDetails=new ResponeDetails(LocalDateTime.now(),message,details, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responeDetails);
    }




}
