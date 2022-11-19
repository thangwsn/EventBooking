package com.eticket.application.websocket.observer;

import java.util.ArrayList;
import java.util.List;


public class Observable<T> {

    public List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        this.observers.add(observer);
    }

    protected void notifyObservers(T source, Integer eventId, String message, MessageType type) {
        for (Observer<T> o : observers) {
            o.handle(new Notification<T>(source, eventId, message, type));
        }
    }
}
