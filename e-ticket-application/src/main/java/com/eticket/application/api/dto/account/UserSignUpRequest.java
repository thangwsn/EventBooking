package com.eticket.application.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class UserSignUpRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Email is not valid")
    private String email;
    @NotBlank(message = "Password is required1234")
    @Length(min = 8, message = "Password is too short")
    private String password;
}
