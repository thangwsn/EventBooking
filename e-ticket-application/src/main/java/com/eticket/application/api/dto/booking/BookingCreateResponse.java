package com.eticket.application.api.dto.booking;

import com.eticket.domain.entity.booking.BookingStatus;
import com.eticket.domain.entity.booking.PaymentType;
import com.eticket.domain.entity.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateResponse {
    private Integer id;
    private String bookingCode;
    private EventType eventType;
    private PaymentType paymentType;
    private BookingStatus status;
    private String fullName;
    private String mobile;
    private String eventTitle;
    private List<String> listTicketCode;
    private int ticketNum;
    private double amount;
    private String message;
}
