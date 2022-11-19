package com.eticket.application.websocket.observer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Notification<T> {
    public T source;
    public Integer eventId;
    public String message;
    public MessageType type;

}
