package com.eticket.application.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EmployeeCreateRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank
    @Email(message = "Email is not valid")
    private String email;
    private String mobile;
    private String fullName;
    private String gender;
    private Date dateOfBirth;
    private Date joinDate;
    private AddressCreateRequest address;
    private String position;
    private String role;
}
