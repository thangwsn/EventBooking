package com.eticket.domain.service;

import com.eticket.application.api.dto.booking.BookingDetailResponse;
import com.eticket.application.api.dto.booking.BookingGetRequest;
import com.eticket.application.api.dto.booking.ListBookingGetResponse;

public interface BookingService {
    void handleBooking(String message);

    void cancelBooking(Integer bookingId);

    ListBookingGetResponse getListBooking(BookingGetRequest bookingGetRequest);

    BookingDetailResponse getBookingDetail(Integer bookingId);
}
