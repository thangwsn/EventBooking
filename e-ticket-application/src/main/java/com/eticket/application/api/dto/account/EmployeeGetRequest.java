package com.eticket.application.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeGetRequest {
    private int pageNo;
    private int pageSize;
    private String sortField;
    private String sortDirection;
    private String searchKey;
}
