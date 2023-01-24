package com.eticket.domain.service;

import com.eticket.application.api.dto.FieldViolation;
import com.eticket.application.api.dto.account.*;
import com.eticket.domain.entity.account.*;
import com.eticket.domain.entity.booking.BookingStatus;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.ResourceNotFoundException;
import com.eticket.domain.repo.*;
import com.eticket.infrastructure.mail.MailService;
import com.eticket.infrastructure.mail.MailType;
import com.eticket.infrastructure.mapper.EmployeeMap;
import com.eticket.infrastructure.mapper.UserMap;
import com.eticket.infrastructure.security.jwt.EncryptionUtil;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import com.eticket.infrastructure.security.service.JwtUserDetailsService;
import com.eticket.infrastructure.utils.Utils;
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
    public List<FieldViolation> registerEmployee(EmployeeCreateRequest employeeCreateRequest) {
        List<FieldViolation> violationList = new ArrayList<>();
        if (existsUsername(employeeCreateRequest.getUsername())) {
            violationList.add(new FieldViolation("username", "Username is already being used"));
        }
        if (existsEmail(employeeCreateRequest.getEmail())) {
            violationList.add(new FieldViolation("email", "Email address is already being used"));
        }
        if (violationList.size() == 0) {
            Employee employee = new Employee();
            employee.setUsername(employeeCreateRequest.getUsername());
            employee.setEmail(employeeCreateRequest.getEmail());
            employee.setMobile(employeeCreateRequest.getMobile());
            employee.setFullName(employee.getFullName());
            employee.setRemoved(false);
            employee.setRole(Role.valueOf(employeeCreateRequest.getRole()));
            if (employeeCreateRequest.getGender() != null) {
                employee.setGender(Gender.valueOf(employeeCreateRequest.getGender()));
            }
            if (employeeCreateRequest.getPosition() != null) {
                employee.setPosition(Position.valueOf(employeeCreateRequest.getPosition()));
            }
            if (employeeCreateRequest.getDateOfBirth() != null) {
                employee.setDateOfBirth(employeeCreateRequest.getDateOfBirth());
            }
            if (employee.getJoinDate() != null) {
                employee.setJoinDate(employee.getJoinDate());
            }
            employee.setEmployeeCode(sequenceEmployeeCode());
            Address address = Address.builder().street(employeeCreateRequest.getAddress().getStreet())
                    .ward(employeeCreateRequest.getAddress().getWard())
                    .district(employeeCreateRequest.getAddress().getDistrict())
                    .city(employeeCreateRequest.getAddress().getCity()).build();
            employee.setAddress(address);
            String randomPassword = RandomStringUtils.randomAlphanumeric(8);
            employee.setPassword(passwordEncoder.encode(randomPassword));
            logger.info(String.format("Password gen: %s", randomPassword));
            mailService.sendMailText(MailType.PASSWORD, randomPassword);
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

    @Override
    public void updateInformation(UpdateInfomationRequest updateInfomationRequest) throws AuthenticationException, ResourceNotFoundException {
        String usernameFromJwtToken = jwtUtils.getUserNameFromJwtToken();
        if (usernameFromJwtToken.isEmpty()) {
            throw new AuthenticationException("401 Unauthorized!");
        }
        Account account = accountRepository.findByUsernameAndRemovedFalse(usernameFromJwtToken)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found account by username is %s ", usernameFromJwtToken)));
        if (updateInfomationRequest.getAddress() != null) {
            if (account.getAddress() != null) {
                Address address = account.getAddress();
                address.setStreet(updateInfomationRequest.getAddress().getStreet());
                address.setWard(updateInfomationRequest.getAddress().getWard());
                address.setDistrict(updateInfomationRequest.getAddress().getDistrict());
                address.setCity(updateInfomationRequest.getAddress().getCity());
                account.setAddress(address);
            } else {
                account.setAddress(updateInfomationRequest.getAddress());
            }
        }
        if (updateInfomationRequest.getGender() != null) {
            account.setGender(Gender.valueOf(updateInfomationRequest.getGender()));
        }
        if (updateInfomationRequest.getDateOfBirth() > 0) {
            account.setDateOfBirth(Utils.convertToDate(updateInfomationRequest.getDateOfBirth()));
        }
        if (updateInfomationRequest.getFullName() != null) {
            account.setFullName(updateInfomationRequest.getFullName());
        }
        accountRepository.save(account);
    }

    @Override
    public EmployeeEditDto getEmployeeForEdit(Integer employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findByRemovedFalseAndId(employeeId).orElseThrow(() -> new ResourceNotFoundException(String.format("Not found employee by employeeId is %s ", employeeId)));
        return employeeMap.toEmployeeEditDto(employee);
    }

    @Override
    public void updateEmployee(Integer employeeId, EmployeeEditDto employeeEditDto) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findByRemovedFalseAndId(employeeId).orElseThrow(() -> new ResourceNotFoundException(String.format("Not found employee by employeeId is %s ", employeeId)));
        if (employeeEditDto.getFullName() != null) {
            employee.setFullName(employeeEditDto.getFullName());
        }
        if (employeeEditDto.getMobile() != null) {
            employee.setMobile(employeeEditDto.getMobile());
        }
        if (employeeEditDto.getGender() != null) {
            employee.setGender(Gender.valueOf(employeeEditDto.getGender()));
        }
        if (employeeEditDto.getPosition() != null) {
            employee.setPosition(Position.valueOf(employeeEditDto.getPosition()));
        }
        if (employeeEditDto.getDateOfBirth() != null) {
            employee.setDateOfBirth(employeeEditDto.getDateOfBirth());
        }
        if (employeeEditDto.getJoinDate() != null) {
            employee.setJoinDate(employeeEditDto.getDateOfBirth());
        }
        if (employeeEditDto.getAddress() != null) {
            Address address = employee.getAddress();
            if (address == null) {
                address = new Address();
            }
            address.setStreet(employeeEditDto.getAddress().getStreet());
            address.setWard(employeeEditDto.getAddress().getWard());
            address.setDistrict(employeeEditDto.getAddress().getDistrict());
            address.setCity(employeeEditDto.getAddress().getCity());
            employee.setAddress(address);
        }
        employeeRepository.saveAndFlush(employee);
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
