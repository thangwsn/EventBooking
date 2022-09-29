package com.eticket.infrastructure.security.service;

import com.eticket.domain.entity.account.Account;
import com.eticket.domain.repo.JpaAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private JpaAccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsernameAndIsActivedTrueAndRemovedFalse(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomUserDetail.build(account);
    }
}
