package com.eticket.domain.service;

import com.eticket.application.api.dto.FieldViolation;
import com.eticket.application.api.dto.account.*;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.ResourceNotFoundException;

import java.util.List;

public interface AccountService {
    LoginResponse authenticate(LoginRequest loginRequest) throws Exception;

    List<FieldViolation> registerUser(UserSignUpRequest userSignUpRequest);

    boolean verifyActiveCode(Integer userId, String code);

    List<FieldViolation> registerEmployee(EmployeeSignUpRequest employeeSignUpRequest);

    AccountInfoResponse getAccountInfo() throws AuthenticationException, ResourceNotFoundException;

    ListUserGetResponse getListUser();

    UserDetailResponse getUserDetail(Integer userId) throws AuthenticationException, ResourceNotFoundException;

    ListEmployeeGetResponse getListEmployee();

    EmployeeDetailResponse getEmployeeDetail(Integer employeeId) throws ResourceNotFoundException;

    void removeEmployee(Integer employeeId);

    UserProfileResponse getUserProfile() throws AuthenticationException, ResourceNotFoundException;

    List<FieldViolation> changePassword(ChangePasswordRequest changePasswordRequest) throws AuthenticationException, ResourceNotFoundException;

    LoginResponse updateToken() throws AuthenticationException;
}
