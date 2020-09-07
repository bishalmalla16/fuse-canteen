package com.fusemachine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CanteenExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ItemNotFoundException ex){
        ApiError error = new ApiError();

        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MenuNotFoundException.class)
    public ResponseEntity<ApiError> handleException(MenuNotFoundException ex){
        ApiError error = new ApiError();

        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ApiError> handleException(NotFoundException ex){
        ApiError error = new ApiError();

        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
