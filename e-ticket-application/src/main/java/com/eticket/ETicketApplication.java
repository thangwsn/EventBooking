package com.eticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableAsync
@EnableScheduling
public class ETicketApplication {
    public static void main(String[] args) {
        SpringApplication.run(ETicketApplication.class, args);
    }

}
