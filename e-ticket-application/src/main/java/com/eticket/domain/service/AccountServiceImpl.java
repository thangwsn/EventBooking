package com.eticket.domain.service;

import com.eticket.application.api.dto.FieldViolation;
import com.eticket.application.api.dto.account.*;
import com.eticket.domain.entity.account.*;
import com.eticket.domain.entity.booking.BookingStatus;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.ResourceNotFoundException;
import com.eticket.domain.repo.*;
import com.eticket.infrastructure.mail.MailService;
import com.eticket.infrastructure.mapper.EmployeeMap;
import com.eticket.infrastructure.mapper.UserMap;
import com.eticket.infrastructure.security.jwt.EncryptionUtil;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import com.eticket.infrastructure.security.service.JwtUserDetailsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private JpaAccountRepository accountRepository;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private JpaEmployeeRepository employeeRepository;
    @Autowired
    private JpaBookingRepository bookingRepository;
    @Autowired
    private JpaFollowRepository followRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtUserDetailsService userDetailService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserMap userMap;
    @Autowired
    private EmployeeMap employeeMap;

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (DisabledException e) {
            logger.error("---Disabled User---{}", e.getMessage());
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailService
                .loadUserByUsername(loginRequest.getUsername());
        final String token = jwtUtils.generateToken(userDetails);
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()).get(0);
        LoginResponse loginResponse = new LoginResponse(userDetails.getUsername(), role, EncryptionUtil.encrypt(token));
        return loginResponse;
    }

    @Override
    public List<FieldViolation> registerUser(UserSignUpRequest userSignUpRequest) {
        List<FieldViolation> violationList = new ArrayList<>();
        if (existsUsername(userSignUpRequest.getUsername())) {
            violationList.add(new FieldViolation("username", "Username is already being used"));
        }
        if (existsEmail(userSignUpRequest.getEmail())) {
            violationList.add(new FieldViolation("email", "Email address is already being used"));
        }

        // do not have invalid
        if (violationList.size() == 0) {
            Random random = new Random();
            String activeCode = RandomStringUtils.randomAlphanumeric(30);
            User user = new User();
            user.setUsername(userSignUpRequest.getUsername());
            user.setEmail(userSignUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));
            user.setActiveCode(activeCode);
            user.setRemoved(true);
            user.setRole(Role.USER);
            user.setAmountReserved(0.0);
            // save db
            user = userRepository.saveAndFlush(user);
            // async send verify mail
            mailService.sendVerificationMail(user.getId(), user.getUsername(), user.getEmail(), user.getActiveCode());
        }
        return violationList;
    }

    @Override
    public boolean verifyActiveCode(Integer userId, String code) {
        User user = userRepository.findByIdAndActiveCode(userId, code).orElse(null);
        if (user == null) {
            return false;
        }
        user.setRemoved(false);
        user.setActiveCode(null);
        user.setUserCode(sequenceUserCode());
        userRepository.save(user);
        return true;
    }

    @Override
    public List<FieldViolation> registerEmployee(EmployeeSignUpRequest employeeSignUpRequest) {
        List<FieldViolation> violationList = new ArrayList<>();
        if (!employeeSignUpRequest.getPassword().equals(employeeSignUpRequest.getAgainPassword())) {
            violationList.add(new FieldViolation("againPassword", ""));
        } else {
            if (existsUsername(employeeSignUpRequest.getUsername())) {
                violationList.add(new FieldViolation("username", "Username is already being used"));
            }
            if (existsEmail(employeeSignUpRequest.getEmail())) {
                violationList.add(new FieldViolation("email", "Email address is already being used"));
            }
        }
        if (violationList.size() == 0) {
            Employee employee = modelMapper.map(employeeSignUpRequest, Employee.class);
            employee.setRemoved(false);
            employee.setRole(Role.valueOf(employeeSignUpRequest.getRoleString()));
            employee.setGender(Gender.valueOf(employeeSignUpRequest.getGenderString()));
            employee.setPosition(Position.valueOf(employeeSignUpRequest.getPositionString()));
            employee.setEmployeeCode(sequenceEmployeeCode());
            employee.setPassword(passwordEncoder.encode(employeeSignUpRequest.getPassword()));
            employeeRepository.saveAndFlush(employee);
        }
        return violationList;
    }

    @Override
    public AccountInfoResponse getAccountInfo() throws AuthenticationException, ResourceNotFoundException {
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        if (usernameFromJwtToken.isEmpty()) {
            throw new AuthenticationException("401 Unauthorized!");
        }
        Account account = accountRepository.findByUsernameAndRemovedFalse(usernameFromJwtToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found account by username is %s ", usernameFromJwtToken)));
        return modelMapper.map(account, AccountInfoResponse.class);
    }

    @Override
    public ListUserGetResponse getListUser() {
        List<User> listUser = userRepository.findByRemovedFalse();
        List<UserGetResponse> listUserGetResponse = listUser.stream().map(u -> userMap.toUserGetResponse(u)).collect(Collectors.toList());
        return new ListUserGetResponse(listUserGetResponse.size(), listUserGetResponse);
    }

    @Override
    public UserDetailResponse getUserDetail(Integer userId) throws AuthenticationException, ResourceNotFoundException {
        Account account = accountRepository.findByUsernameAndRemovedFalse(jwtUtils.getUserNameFromJwtToken())
                .orElseThrow(() -> new AuthenticationException("401 Unauthorized!"));
        boolean userView = account.getRole().equals(Role.USER);
        if (userView && !account.getId().equals(userId)) {
            throw new RuntimeException("");
        }
        User user = userRepository.findByIdAndRemovedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found user by userId is %s ", userId)));
        return userMap.toUserDetailResponse(user, getBookingNum(userId), getFollowedNum(userId));
    }

    @Override
    public ListEmployeeGetResponse getListEmployee() {
        List<Employee> employeeList = employeeRepository.findByRemovedFalse();
        List<EmployeeGetResponse> employeeGetResponses = employeeList.stream().map(e -> employeeMap.toEmployeeGetResponse(e)).collect(Collectors.toList());
        return new ListEmployeeGetResponse(employeeGetResponses.size(), employeeGetResponses);
    }

    @Override
    public EmployeeDetailResponse getEmployeeDetail(Integer employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findByRemovedFalseAndId(employeeId).orElseThrow(() -> new ResourceNotFoundException(String.format("Not found employee by employeeId is %s ", employeeId)));
        return employeeMap.toEmployeeDetailResponse(employee);
    }

    @Override
    public void removeEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findByRemovedFalseAndId(employeeId).orElseThrow(() -> new RuntimeException(""));
        employee.setRemoved(true);
        employeeRepository.saveAndFlush(employee);
    }

    @Override
    public UserProfileResponse getUserProfile() throws AuthenticationException, ResourceNotFoundException {
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        if (usernameFromJwtToken.isEmpty()) {
            throw new AuthenticationException("401 Unauthorized!");
        }
        User user = userRepository.findByUsernameAndRemovedFalse(usernameFromJwtToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found user by username is %s ", usernameFromJwtToken)));
        return userMap.toUserProfileResponse(user);
    }

    @Override
    public List<FieldViolation> changePassword(ChangePasswordRequest changePasswordRequest) throws AuthenticationException, ResourceNotFoundException {
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        if (usernameFromJwtToken.isEmpty()) {
            throw new AuthenticationException("401 Unauthorized!");
        }
        Account account = accountRepository.findByUsernameAndRemovedFalse(usernameFromJwtToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found account by username is %s ", usernameFromJwtToken)));
        List<FieldViolation> violationList = new ArrayList<>();
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), account.getPassword())) {
            violationList.add(new FieldViolation("currentPassword", "Current password is not match"));
            return violationList;
        }
        account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        accountRepository.save(account);
        return violationList;
    }

    @Override
    public LoginResponse updateToken() throws AuthenticationException {
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        if (usernameFromJwtToken.isEmpty()) {
            throw new AuthenticationException("401 Unauthorized!");
        }
        final UserDetails userDetails = userDetailService
                .loadUserByUsername(usernameFromJwtToken);
        final String token = jwtUtils.generateToken(userDetails);
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()).get(0);
        LoginResponse loginResponse = new LoginResponse(userDetails.getUsername(), role, EncryptionUtil.encrypt(token));
        return loginResponse;
    }

    private boolean existsUsername(String username) {
        return accountRepository.existsAccountByUsernameAndRemovedFalse(username);
    }

    private boolean existsEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    private String sequenceUserCode() {
        String latestUserCode = userRepository.latestUserCode().orElse("USER000000");
        return new StringBuffer().append(latestUserCode.substring(0, 4))
                .append(String.format("%06d", Integer.parseInt(latestUserCode.substring(4)) + 1))
                .toString();
    }

    private String sequenceEmployeeCode() {
        String latestEmployeeCode = employeeRepository.latestEmployeeCode().orElse("EMP000000");
        return new StringBuffer().append(latestEmployeeCode.substring(0, 3))
                .append(String.format("%06d", Integer.parseInt(latestEmployeeCode.substring(4)) + 1))
                .toString();
    }

    private int getBookingNum(Integer userId) {
        return bookingRepository.countBookingByRemovedFalseAndStatusAndUserId(BookingStatus.COMPLETED, userId);
    }

    private int getFollowedNum(Integer userId) {
        return followRepository.countByUserIdAndRemovedFalse(userId);
    }
}
