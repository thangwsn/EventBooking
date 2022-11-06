package com.eticket.infrastructure.mapper;

import com.eticket.application.api.dto.booking.*;
import com.eticket.domain.entity.booking.Booking;
import com.eticket.domain.entity.booking.BookingStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMap {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventMap eventMap;

    public BookingGetResponse toBookingGet(Booking booking, List<BookingCatalogItem> ticketCatalogList) {
        BookingGetResponse bookingGetResponse = modelMapper.map(booking, BookingGetResponse.class);
        bookingGetResponse.setStatusString(booking.getStatus().toString());
        bookingGetResponse.setUsername(booking.getUser().getUsername());
        bookingGetResponse.setCreateTime(booking.getCreateTime().getTime());
        bookingGetResponse.setTicketCatalogList(ticketCatalogList);
        return bookingGetResponse;
    }

    public BookingDetailResponse toBookingDetail(Booking booking, List<BookingCatalogItem> ticketCatalogList, List<TicketGetResponse> ticketList, PaymentGetResponse paymentGetResponse, boolean userView) {
        BookingDetailResponse bookingDetailResponse = modelMapper.map(booking, BookingDetailResponse.class);
        bookingDetailResponse.setStatusString(booking.getStatus().toString());
        bookingDetailResponse.setUserId(booking.getUser().getId());
        bookingDetailResponse.setUsername(booking.getUser().getUsername());
        bookingDetailResponse.setCreateTime(booking.getCreateTime().getTime());
        bookingDetailResponse.setEmail(booking.getUser().getEmail());
        bookingDetailResponse.setTicketCatalogList(ticketCatalogList);
        bookingDetailResponse.setPayment(paymentGetResponse);
        if (userView) {
            bookingDetailResponse.setListTicket(ticketList);
        }
        bookingDetailResponse.setEvent(eventMap.toEventGetResponse(booking.getEvent(), -1, userView));
        if (booking.getStatus().equals(BookingStatus.PENDING)) {
            bookingDetailResponse.setBookingTimeout(booking.getBookingTimeout().getTime());
        }
        return bookingDetailResponse;
    }
}
