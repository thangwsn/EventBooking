package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketGetResponse {
    private Integer id;
    private String code;
    private double price;
    private long soldTime;
    private String eventTitle;
    private long startTime;
    private String locationString;
    private String ticketCatalogTitle;
}
