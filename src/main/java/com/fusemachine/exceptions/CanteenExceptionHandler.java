package com.fusemachine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CanteenExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InvalidArgumentException.class)
    public ResponseEntity<ApiError> handleException(InvalidArgumentException ex){
        ApiError error = new ApiError();

        error.setStatus(HttpStatus.FORBIDDEN);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ResourcesNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourcesNotFoundException ex){
        ApiError error = new ApiError();

        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CannotAlterException.class)
    public ResponseEntity<ApiError> handleException(CannotAlterException ex){
        ApiError error = new ApiError();

        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleException(InvalidCredentialsException ex){
        ApiError error = new ApiError();

        error.setStatus(HttpStatus.UNAUTHORIZED);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

}
