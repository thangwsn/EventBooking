package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingGetResponse {
    private Integer id;
    private String code;
    private double amount;
    private String statusString;
    private String fullName;
    private String mobile;
    private String username;
    private long createTime;
    private List<BookingCatalogItem> ticketCatalogList;
}
