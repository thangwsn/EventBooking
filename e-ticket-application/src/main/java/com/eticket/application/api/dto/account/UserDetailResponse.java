package com.eticket.application.api.dto.account;

import com.eticket.domain.entity.account.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private Integer id;
    private String username;
    private String email;
    private String mobile;
    private String fullName;
    private Gender gender;
    private Date dateOfBirth;
    private String imageUrl;
    private String addressString;
    private double amountReserved;
    private String userCode;
    private int bookingNum;
    private int followedNum;
}
