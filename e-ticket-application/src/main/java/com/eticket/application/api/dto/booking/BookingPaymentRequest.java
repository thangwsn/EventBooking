package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingPaymentRequest {
    private Integer bookingId;
    private double amount;
    private String paymentType;
}
