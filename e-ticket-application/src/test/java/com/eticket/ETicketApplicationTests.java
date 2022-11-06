package com.eticket;

import com.eticket.domain.entity.account.Account;
import com.eticket.domain.repo.JpaAccountRepository;
import com.eticket.domain.repo.JpaEmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ETicketApplicationTests {

    @Autowired
    JpaAccountRepository accountRepository;

    @Autowired
    JpaEmployeeRepository employeeRepository;


    @Test
    void contextLoads() {
    }

    @Test
    void testGetAccountByUsername() {
        Optional<Account> account = accountRepository.findByUsernameAndRemovedFalse("komukai");
        System.out.println(account.get().getUsername());
    }

}
