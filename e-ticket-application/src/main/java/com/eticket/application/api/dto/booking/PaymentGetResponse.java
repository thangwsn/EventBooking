package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGetResponse {
    private Integer id;
    private String code;
    private long createTime;
    private String typeString;
}
