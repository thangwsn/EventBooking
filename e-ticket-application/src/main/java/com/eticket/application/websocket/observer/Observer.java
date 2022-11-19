package com.eticket.application.websocket.observer;


public interface Observer<T> {
    void handle(Notification<T> args);
}