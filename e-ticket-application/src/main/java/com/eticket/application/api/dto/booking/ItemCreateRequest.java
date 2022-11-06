package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest {
    private int ticketCatalogId;
    private int quantity;
}
