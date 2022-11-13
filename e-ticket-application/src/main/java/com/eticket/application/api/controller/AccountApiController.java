package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.FieldViolation;
import com.eticket.application.api.dto.account.*;
import com.eticket.domain.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AccountApiController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> authenticateAccount(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse response = accountService.authenticate(loginRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @PostMapping("/signup-user")
    public ResponseEntity<BaseResponse> registerUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        List<FieldViolation> violationList = accountService.registerUser(userSignUpRequest);
        if (violationList.size() > 0) {
            return ResponseEntity.ok(BaseResponse.ofInvalid(violationList));
        }
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PostMapping("/verify")
    public String verifyCodeSignUp(@RequestParam("user_id") Integer userId, @RequestParam("code") String code) {
        boolean accept = accountService.verifyActiveCode(userId, code);
        return "";
    }

    @PostMapping("/signup-employee")
    public ResponseEntity<BaseResponse> registerEmployee(@Valid @RequestBody EmployeeSignUpRequest employeeSignUpRequest) {
        List<FieldViolation> violationList = accountService.registerEmployee(employeeSignUpRequest);
        if (violationList.size() > 0) {
            ResponseEntity.ok(BaseResponse.ofInvalid(violationList));
        }
        ;
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PostMapping("/update-information")
    public ResponseEntity<?> updateInformation() {
        return null;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword() {
        return null;
    }

    @GetMapping("/account-info")
    public ResponseEntity<BaseResponse<AccountInfoResponse>> getAccountInfo() {
        AccountInfoResponse response = accountService.getAccountInfo();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/verify-register")
    public ResponseEntity<BaseResponse> verifyRegister(@RequestParam("user_id") Integer userId, @RequestParam("active_code") String activeCode) {
        boolean verified = accountService.verifyActiveCode(userId, activeCode);
        if (!verified) {
            return ResponseEntity.ok(BaseResponse.ofInvalid(verified));
        }
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }
}
