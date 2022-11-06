package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateResponse {
    private int ticketCatalogId;
    private int quantity;
    private List<String> ListTicketCode;
}
