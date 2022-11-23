package com.eticket.domain.service;

import com.eticket.application.api.dto.booking.BookingPaymentRequest;
import com.eticket.application.websocket.observer.MessageType;
import com.eticket.application.websocket.observer.Observable;
import com.eticket.domain.entity.account.User;
import com.eticket.domain.entity.booking.Booking;
import com.eticket.domain.entity.booking.BookingStatus;
import com.eticket.domain.entity.booking.PaymentStatus;
import com.eticket.domain.entity.booking.PaymentType;
import com.eticket.domain.entity.event.Ticket;
import com.eticket.domain.entity.quartz.ScheduleJob;
import com.eticket.domain.entity.quartz.ScheduleJobType;
import com.eticket.domain.exception.ResourceNotFoundException;
import com.eticket.domain.repo.*;
import com.eticket.infrastructure.mail.MailService;
import com.eticket.infrastructure.paypal.PayPalService;
import com.eticket.infrastructure.quartz.service.ScheduleService;
import com.eticket.infrastructure.utils.Constants;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl extends Observable<PaymentServiceImpl> implements PaymentService {
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
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private JpaTicketRepository ticketRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private JpaUserRepository userRepository;

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
    public Integer completePayment(String paymentId, String payerId) throws ResourceNotFoundException {
        Map<String, Object> response = payPalService.completePayment(paymentId, payerId);
        Payment payment = (Payment) response.get("payment");
        Integer bookingId = Integer.parseInt(payment.getTransactions().get(0).getDescription());
        // handle save db
        Booking booking = bookingRepository.findByIdAndRemovedFalse(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found booking by id is %s ", bookingId)));
        User user = booking.getUser();
        if (user == null) {
            throw new RuntimeException("");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        List<Ticket> ticketList = booking.getListTicket();
        for (Ticket ticket : ticketList) {
            ticket.setQRcode(fileStorageService.generateQRCode(ticket));
        }
        // notify
        notifyObservers(this, booking.getEvent().getId(), booking.getUser().getUsername() + " has booked ", MessageType.BOOKING);
        // send an email for all ticket
        mailService.sendMailAttachment(user.getUsername(), user.getEmail(), ticketList);
        booking.setListTicket(ticketList);
        bookingRepository.save(booking);
        ticketRepository.saveAll(ticketList);
        com.eticket.domain.entity.booking.Payment paymentEnitity = com.eticket.domain.entity.booking.Payment.builder()
                .code(payment.getId()).amount(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()))
                .booking(booking).type(PaymentType.PayPal).status(PaymentStatus.SUCCESS).build();
        paymentRepository.save(paymentEnitity);
        // quartz schedule: cancel booking timeout job
        Optional<ScheduleJob> scheduleJobOptional = scheduleJobRepository.findByRemovedFalseAndObjectIdAndType(bookingId, ScheduleJobType.BOOKING_TIMEOUT);
        if (scheduleJobOptional.isPresent()) {
            scheduleService.cancelJob(scheduleJobOptional.get().getJobKey(), Constants.JOB_GROUP_BOOKING);
        }
        return bookingId;
    }
}
