package com.fusemachine.exceptions;

public class CannotAlterException extends RuntimeException {
    public CannotAlterException(String message) {
        super(message);
    }

    public CannotAlterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotAlterException(Throwable cause) {
        super(cause);
    }
}
