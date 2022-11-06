package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventGetRequest {
    private int pageNo;
    private int pageSize;
    private String sortField;
    private String sortDirection;
    private String type;
    private String status;
    private String searchKey;
}
