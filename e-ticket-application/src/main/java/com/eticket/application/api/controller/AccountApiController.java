package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.account.*;
import com.eticket.domain.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountApiController {
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> authenticateAccount(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = accountService.authenticate(loginRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @PostMapping("/signup-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        return null;
    }

    @PostMapping("/verify-signup")
    public ResponseEntity<?> verifyCodeSignUp(@Valid @RequestBody VerifySignUpRequest verifySignUpRequest) {
        return null;
    }

    @PostMapping("/signup-employee")
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody EmployeeSignUpRequest employeeSignUpRequest) {
        return null;
    }

    @PostMapping("/update-information")
    public ResponseEntity<?> updateInformation() {
        return null;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword() {
        return null;
    }
}
