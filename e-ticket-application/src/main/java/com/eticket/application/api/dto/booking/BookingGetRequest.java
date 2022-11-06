package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingGetRequest {
    private int pageNo;
    private int pageSize;
    private String sortField;
    private String sortDirection;
    private String status;
    private String searchKey;
}
