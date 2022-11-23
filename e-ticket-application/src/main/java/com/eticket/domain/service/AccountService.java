package com.eticket.domain.service;

import com.eticket.application.api.dto.FieldViolation;
import com.eticket.application.api.dto.account.*;

import java.util.List;

public interface AccountService {
    LoginResponse authenticate(LoginRequest loginRequest) throws Exception;

    List<FieldViolation> registerUser(UserSignUpRequest userSignUpRequest);

    boolean verifyActiveCode(Integer userId, String code);

    List<FieldViolation> registerEmployee(EmployeeSignUpRequest employeeSignUpRequest);

    AccountInfoResponse getAccountInfo();

    ListUserGetResponse getListUser(UserGetRequest userGetRequest);

    UserDetailResponse getUserDetail(Integer userId);

    ListEmployeeGetResponse getListEmployee(EmployeeGetRequest employeeGetRequest);

    EmployeeDetailResponse getEmployeeDetail(Integer employeeId);

    void removeEmployee(Integer employeeId);
}
