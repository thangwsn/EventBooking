package com.eticket.infrastructure.security.service;

import com.eticket.domain.entity.account.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomUserDetail implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String email;

    private String username;
    private String password;
    private GrantedAuthority authority;

    public CustomUserDetail(Integer id, String email, String username, String password, GrantedAuthority authority) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public static CustomUserDetail build(Account account) {
        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole().name());

        return new CustomUserDetail(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getPassword(),
                authority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(authority);
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomUserDetail account = (CustomUserDetail) o;
        return Objects.equals(id, account.id);
    }
}
