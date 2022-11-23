package com.eticket.domain.service;

import com.eticket.application.api.dto.booking.BookingPaymentRequest;
import com.eticket.domain.exception.ResourceNotFoundException;

public interface PaymentService {
    Object createPayment(BookingPaymentRequest bookingPaymentRequest);

    Integer completePayment(String paymentId, String payerId) throws ResourceNotFoundException;
}
