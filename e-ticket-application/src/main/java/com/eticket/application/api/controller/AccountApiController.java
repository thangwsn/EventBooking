package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.FieldViolation;
import com.eticket.application.api.dto.account.*;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.ResourceNotFoundException;
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

    @GetMapping("/verify-register")
    public ResponseEntity<BaseResponse> verifyRegister(@RequestParam("user_id") Integer userId, @RequestParam("active_code") String activeCode) {
        boolean verified = accountService.verifyActiveCode(userId, activeCode);
        if (!verified) {
            return ResponseEntity.ok(BaseResponse.ofInvalid(verified));
        }
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PostMapping("/update-information")
    public ResponseEntity<?> updateInformation() {
        return null;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws AuthenticationException, ResourceNotFoundException {
        List<FieldViolation> violationList = accountService.changePassword(changePasswordRequest);
        if (violationList.size() > 0) {
            return ResponseEntity.ok(BaseResponse.ofInvalid(violationList));
        }
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping("/update-token")
    public ResponseEntity<BaseResponse<LoginResponse>> updateToken() throws AuthenticationException {
        LoginResponse response = accountService.updateToken();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword() {
        return null;
    }

    @GetMapping("/account-info")
    public ResponseEntity<BaseResponse<AccountInfoResponse>> getAccountInfo() throws AuthenticationException, ResourceNotFoundException {
        AccountInfoResponse response = accountService.getAccountInfo();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/users")
    public ResponseEntity<BaseResponse<ListUserGetResponse>> getUserList() {
        ListUserGetResponse response = accountService.getListUser();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<BaseResponse<UserDetailResponse>> getDetailUser(@PathVariable("user_id") Integer userId) throws AuthenticationException, ResourceNotFoundException {
        UserDetailResponse response = accountService.getUserDetail(userId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/user-profile")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getUserProfile() throws AuthenticationException, ResourceNotFoundException {
        UserProfileResponse response = accountService.getUserProfile();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
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

    @GetMapping("/employees")
    public ResponseEntity<BaseResponse<ListEmployeeGetResponse>> getListEmployee() {
        ListEmployeeGetResponse response = accountService.getListEmployee();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/employees/{employee_id}")
    public ResponseEntity<BaseResponse<EmployeeDetailResponse>> getDetailEmployee(@PathVariable("employee_id") Integer employeeId) throws ResourceNotFoundException {
        EmployeeDetailResponse employeeDetailResponse = accountService.getEmployeeDetail(employeeId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(employeeDetailResponse));
    }

    @DeleteMapping("/employees/{employee_id}")
    public ResponseEntity<BaseResponse> removeEmployee(@PathVariable("employee_id") Integer employeeId) {
        accountService.removeEmployee(employeeId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }
}
