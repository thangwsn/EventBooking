package com.eticket.domain.repo;

import com.eticket.domain.entity.account.User;
import com.eticket.domain.entity.booking.Booking;
import com.eticket.domain.entity.booking.BookingStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaBookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByIdAndRemovedFalse(Integer bookingId);

    Optional<Booking> findByIdAndUserAndRemovedFalse(Integer bookingId, User user);

    List<Booking> findByRemovedFalseAndUserAndStatus(User user, String status, Pageable pageable);

    List<Booking> findByRemovedFalseAndUser(User user, Pageable pageable);

    List<Booking> findByRemovedFalse(Pageable pageable);

    List<Booking> findAllByRemovedFalse();

    Optional<Booking> findByRemovedFalseAndEventIdAndUserIdAndStatus(Integer eventId, Integer userId, BookingStatus status);

    Integer countBookingByRemovedFalseAndStatusAndUserId(BookingStatus status, Integer userId);
}
