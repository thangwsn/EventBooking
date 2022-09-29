package com.eticket.domain.service;

import com.eticket.application.api.dto.account.LoginRequest;
import com.eticket.application.api.dto.account.LoginResponse;

public interface AccountService {
    LoginResponse authenticate(LoginRequest loginRequest);
}
