package com.eticket.application.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class ChangePasswordRequest {
    @NotBlank(message = "Password is required")
    @Length(min = 8, message = "Password is too short")
    private String currentPassword;
    @NotBlank(message = "New password is required")
    @Length(min = 8, message = "New password is too short")
    private String newPassword;
}
