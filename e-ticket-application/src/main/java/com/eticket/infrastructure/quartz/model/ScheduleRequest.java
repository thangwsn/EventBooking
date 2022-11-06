package com.eticket.infrastructure.quartz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
    private String jobKey;
    private String jobGroup;
    private String triggeGroup;
    private String type;
    private Integer objectId; // bookingId | eventId
    private Timestamp startAt;
}
