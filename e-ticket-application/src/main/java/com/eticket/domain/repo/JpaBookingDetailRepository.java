package com.eticket.domain.repo;

import com.eticket.domain.entity.booking.Booking;
import com.eticket.domain.entity.booking.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaBookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    List<BookingDetail> findAllByBooking(Booking booking);
}
