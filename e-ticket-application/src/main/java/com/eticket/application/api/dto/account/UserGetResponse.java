package com.eticket.application.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetResponse {
    private Integer id;
    private String username;
    private String email;
    private String mobile;
    private String fullName;
    private double amountReserved;
    private String userCode;
}
