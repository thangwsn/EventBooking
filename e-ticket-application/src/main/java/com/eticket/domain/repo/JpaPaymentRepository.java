package com.eticket.domain.repo;

import com.eticket.domain.entity.booking.Booking;
import com.eticket.domain.entity.booking.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByBooking(Booking booking);
}
