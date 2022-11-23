package com.eticket.application.api.dto.account;

import com.eticket.domain.entity.account.Gender;
import com.eticket.domain.entity.account.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponse {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String fullName;
    private Gender gender;
    private Date dateOfBirth;
    private String imageUrl;
    private String addressString;
    private String employeeCode;
    private Position position;
    private Date joinDate;
}
