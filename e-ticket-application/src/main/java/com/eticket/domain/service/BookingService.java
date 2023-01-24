package com.eticket.domain.service;

import com.eticket.application.api.dto.booking.BookingDetailResponse;
import com.eticket.application.api.dto.booking.BookingGetRequest;
import com.eticket.application.api.dto.booking.ListBookingGetResponse;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.AuthorizationException;
import com.eticket.domain.exception.ResourceNotFoundException;

public interface BookingService {
    void handleBooking(String message) throws ResourceNotFoundException;

    void cancelBooking(Integer bookingId, boolean withAuth) throws AuthenticationException, AuthorizationException, ResourceNotFoundException;

    ListBookingGetResponse getListBooking(BookingGetRequest bookingGetRequest) throws AuthenticationException;

    BookingDetailResponse getBookingDetail(Integer bookingId) throws AuthenticationException, ResourceNotFoundException;

    ListBookingGetResponse getAllBooking();

    boolean removeBooking(Integer bookingId) throws AuthenticationException, AuthorizationException, ResourceNotFoundException;
}
