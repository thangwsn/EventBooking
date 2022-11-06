package com.eticket.application.api.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {
    private String bookingCode;
    private Integer eventId;
    private List<ItemCreateRequest> listItem;
    private String paymentType;
    private String fullName;
    private String mobile;
    private String username;
}
