package com.eticket.infrastructure.kafka.model;

import com.eticket.infrastructure.kafka.enumeration.NotificationEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationEventMessage {
    private int eventId;
    private NotificationEventType type;
    private String message;
}
