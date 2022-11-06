package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.booking.BookingDetailResponse;
import com.eticket.application.api.dto.booking.BookingGetRequest;
import com.eticket.application.api.dto.booking.ListBookingGetResponse;
import com.eticket.domain.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/emp/booking")
public class BookingApiController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/get-list-booking")
    public ResponseEntity<BaseResponse<ListBookingGetResponse>> getListBooking(@RequestBody BookingGetRequest bookingGetRequest) {
        ListBookingGetResponse response = bookingService.getListBooking(bookingGetRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/{booking_id}")
    public ResponseEntity<BaseResponse<BookingDetailResponse>> getBookingDetail(@PathVariable("booking_id") Integer bookingId) {
        BookingDetailResponse response = bookingService.getBookingDetail(bookingId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }
}
