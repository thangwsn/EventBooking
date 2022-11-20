package com.eticket.application.websocket.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    public Integer eventId;
    public String message;
    public MessageType type;
    public Timestamp notifyAt;

}
