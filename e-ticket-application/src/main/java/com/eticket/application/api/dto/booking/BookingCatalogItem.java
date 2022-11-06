package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCatalogItem {
    private Integer ticketCatalogId;
    private String ticketCatalogTitle;
    private double price;
    private int quantity;
}
