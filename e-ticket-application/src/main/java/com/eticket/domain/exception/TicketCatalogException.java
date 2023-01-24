package com.eticket.domain.exception;

public class TicketCatalogException extends Exception {
    public TicketCatalogException(String message) {
        super(message);
    }

    public TicketCatalogException(String message, Throwable cause) {
        super(message, cause);
    }
}
