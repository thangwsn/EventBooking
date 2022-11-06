package com.eticket.application.api.dto.booking;

import com.eticket.application.api.dto.event.EventGetResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailResponse {
    private Integer id;
    private String code;
    private double amount;
    private String statusString;
    private String fullName;
    private String mobile;
    private Integer userId;
    private String username;
    private String email;
    private List<BookingCatalogItem> ticketCatalogList;
    private List<TicketGetResponse> listTicket;
    private PaymentGetResponse payment;
    private long createTime;
    private long bookingTimeout;
    private EventGetResponse event;
}
