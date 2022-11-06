package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCatalogGetResponse {
    private Integer id;
    private String title;
    private int slot;
    private int soldSlot;
    private int remainSlot;
    private double price;
    private String remark;
}
