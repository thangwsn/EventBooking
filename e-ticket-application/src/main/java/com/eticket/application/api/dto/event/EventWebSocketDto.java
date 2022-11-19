package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventWebSocketDto {
    private Integer id;
    private String title;
    private String statusString;
    private double totalSlot;
    private int soldSlot;
    private int remainSlot;
    private int followerNum;
    private List<TicketCatalogGetResponse> ticketCatalogList;
}
