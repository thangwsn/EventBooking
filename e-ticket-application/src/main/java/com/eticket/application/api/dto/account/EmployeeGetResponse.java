package com.eticket.application.api.dto.account;

import com.eticket.domain.entity.account.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeGetResponse {
    private Integer id;
    private String username;
    private String email;
    private String mobile;
    private String fullName;
    private String employeeCode;
    private Position position;
}
