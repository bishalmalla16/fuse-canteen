package com.fusemachine.exceptions;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(String message) {
        super(message);
    }

    public MenuNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuNotFoundException(Throwable cause) {
        super(cause);
    }
}
