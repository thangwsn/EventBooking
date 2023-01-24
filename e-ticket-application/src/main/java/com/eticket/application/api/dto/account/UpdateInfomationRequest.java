package com.eticket.application.api.dto.account;

import com.eticket.domain.entity.account.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInfomationRequest {
    private String fullName;
    private String gender;
    private long dateOfBirth;
    private String imageUrl;
    private Address address;
}
