package com.eticket.domain.service;

import com.eticket.application.api.dto.booking.BookingPaymentRequest;
import com.eticket.domain.entity.booking.Booking;
import com.eticket.domain.entity.booking.BookingStatus;
import com.eticket.domain.entity.booking.PaymentStatus;
import com.eticket.domain.entity.booking.PaymentType;
import com.eticket.domain.entity.quartz.ScheduleJob;
import com.eticket.domain.entity.quartz.ScheduleJobType;
import com.eticket.domain.repo.JpaBookingRepository;
import com.eticket.domain.repo.JpaPaymentRepository;
import com.eticket.domain.repo.JpaScheduleJobRepository;
import com.eticket.infrastructure.paypal.PayPalService;
import com.eticket.infrastructure.quartz.service.ScheduleService;
import com.eticket.infrastructure.utils.Constants;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PayPalService payPalService;
    @Autowired
    private JpaBookingRepository bookingRepository;
    @Autowired
    private JpaPaymentRepository paymentRepository;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private JpaScheduleJobRepository scheduleJobRepository;

    @Override
    public Object createPayment(BookingPaymentRequest bookingPaymentRequest) {
        PaymentType paymentType = PaymentType.valueOf(bookingPaymentRequest.getPaymentType());
        if (paymentType.equals(PaymentType.PayPal)) {
            Map<String, Object> paymentInfo = payPalService.createPayment(bookingPaymentRequest);
            return paymentInfo;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completePayment(String paymentId, String payerId) {
        Map<String, Object> response = payPalService.completePayment(paymentId, payerId);
        Payment payment = (Payment) response.get("payment");
        Integer bookingId = Integer.parseInt(payment.getTransactions().get(0).getDescription());
        // handle save db
        Booking booking = bookingRepository.findByIdAndRemovedFalse(bookingId)
                .orElseThrow(() -> new RuntimeException(String.format("Booking %d not found ", bookingId)));
        ;
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);
        com.eticket.domain.entity.booking.Payment paymentEnitity = com.eticket.domain.entity.booking.Payment.builder()
                .code(payment.getId()).amount(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()))
                .booking(booking).type(PaymentType.PayPal).status(PaymentStatus.SUCCESS).build();
        paymentRepository.save(paymentEnitity);
        // quartz schedule: cancel booking timeout job
        Optional<ScheduleJob> scheduleJobOptional = scheduleJobRepository.findByRemovedFalseAndObjectIdAndType(bookingId, ScheduleJobType.BOOKING_TIMEOUT);
        if (!scheduleJobOptional.isPresent()) {
            return;
        }
        scheduleService.cancelJob(scheduleJobOptional.get().getJobKey(), Constants.JOB_GROUP_BOOKING);
    }
}
