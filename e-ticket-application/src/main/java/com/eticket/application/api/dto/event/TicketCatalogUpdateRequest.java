package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCatalogUpdateRequest {
    private Integer id;
    private String title;
    private int slot;
    private double price;
    private String remark;
}
