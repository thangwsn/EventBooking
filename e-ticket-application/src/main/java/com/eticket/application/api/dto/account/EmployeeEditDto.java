package com.eticket.application.api.dto.account;

import com.eticket.domain.entity.account.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEditDto {
    private Integer id;
    private String username;
    private String email;
    private String mobile;
    private String fullName;
    private String gender;
    private Date dateOfBirth;
    private String imageUrl;
    private Address address;
    private String employeeCode;
    private String position;
    private Date joinDate;
    private String role;
}
