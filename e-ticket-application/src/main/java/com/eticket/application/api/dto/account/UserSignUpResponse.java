package com.eticket.application.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSignUpResponse {
    private String username;
    private String email;
}
