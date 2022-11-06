package com.eticket.application.api.dto.account;

import com.eticket.domain.entity.account.Address;
import com.eticket.domain.entity.account.Gender;
import com.eticket.domain.entity.account.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EmployeeSignUpRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is require")
    @Length(min = 8, message = "Password is too short")
    private String password;
    @NotBlank(message = "Again password is require")
    private String againPassword;
    @Email(message = "Email is not valid")
    private String email;
    @NotBlank(message = "Full name is require")
    private String roleString;
    private String fullName;
    private String genderString;
    private Date dateOfBirth;
    private Date joinDate;
    private AddressCreateRequest address;
    private String positionString;
}
