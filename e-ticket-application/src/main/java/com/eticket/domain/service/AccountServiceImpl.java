package com.eticket.domain.service;

import com.eticket.application.api.dto.account.LoginRequest;
import com.eticket.application.api.dto.account.LoginResponse;
import com.eticket.domain.repo.JpaAccountRepository;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import com.eticket.infrastructure.security.service.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    private JpaAccountRepository accountRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()).get(0);
        return new LoginResponse(userDetails.getUsername(), role, jwt);
    }
}
