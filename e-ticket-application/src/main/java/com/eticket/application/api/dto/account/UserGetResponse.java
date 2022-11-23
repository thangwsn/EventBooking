package com.eticket.application.api.dto.account;

import com.eticket.domain.entity.account.Gender;
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
    private Gender gender;
    private double amountReserved;
    private String userCode;
}
