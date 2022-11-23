package com.eticket.domain.exception;

public class EventRemoveException extends Exception {
    public EventRemoveException(String message) {
        super(message);
    }

    public EventRemoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
