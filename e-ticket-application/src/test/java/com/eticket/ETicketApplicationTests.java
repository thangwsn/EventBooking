package com.eticket;

import com.eticket.domain.entity.account.Account;
import com.eticket.domain.repo.JpaAccountRepository;
import com.eticket.domain.repo.JpaEmployeeRepository;
import com.eticket.domain.repo.JpaTicketRepository;
import com.eticket.domain.repo.JpaUserRepository;
import com.eticket.infrastructure.mail.MailService;
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
    @Autowired
    MailService mailService;
    @Autowired
    JpaUserRepository userRepository;
    @Autowired
    JpaTicketRepository ticketRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetAccountByUsername() {
        Optional<Account> account = accountRepository.findByUsernameAndRemovedFalse("komukai");
        System.out.println(account.get().getUsername());
    }

    @Test
    void testSendMail() {
        mailService.sendVerificationMail(1, "thangnd3", "nguyenduythangk6@gmail.com", "ygrsijwhfdkjahgjfseg");
    }

    @Test
    void testSendEmailAttachment() {
//        mailService.sendMailAttachment("thangnd3", "nguyenduythangk6@gmail.com", Ticket.builder().code("stiuwtiruhtr").QRcode("/upload/static/image/event/1/6/E00010006N00004.png").build());
    }

    @Test
    void testGetLastUser() {
        System.out.println(userRepository.latestUserCode());
    }

    @Test
    void testSearchEvent() {
//        EventGetRequest eventGetRequest = new EventGetRequest(1, 8, "startTime", "desc", "");
    }


}
