package com.eticket.infrastructure.security.service;

import com.eticket.domain.entity.account.Account;
import com.eticket.domain.repo.JpaAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);
    @Autowired
    private JpaAccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsernameAndRemovedFalse(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        if (null != account && account.getUsername().equals(username)) {
            Set<SimpleGrantedAuthority> authorities = new HashSet<>(1);
            authorities.add(new SimpleGrantedAuthority(account.getRole().name()));
            return new User(account.getUsername(), account.getPassword(), true, true, true, !account.isRemoved(), authorities);
        } else {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
    }
}
