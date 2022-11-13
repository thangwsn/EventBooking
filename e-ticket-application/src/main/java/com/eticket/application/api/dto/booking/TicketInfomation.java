package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketInfomation {
    private String code;
    private String catalog;
    private double price;
    private int eventId;
    private String eventTitle;
    private Timestamp bookAt;
    private String fullName;
}
