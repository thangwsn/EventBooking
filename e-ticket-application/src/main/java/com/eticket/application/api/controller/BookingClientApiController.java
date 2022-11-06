package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.booking.*;
import com.eticket.domain.service.BookingService;
import com.eticket.domain.service.PaymentService;
import com.eticket.infrastructure.kafka.consumer.KafkaReceiverSingletonBooking;
import com.eticket.infrastructure.kafka.producer.KafkaSendService;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import com.eticket.infrastructure.utils.Constants;
import com.eticket.infrastructure.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/api/booking")
public class BookingClientApiController {
    Logger log = LoggerFactory.getLogger(BookingClientApiController.class);
    @Autowired
    private KafkaSendService kafkaSendService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ReactiveKafkaConsumerTemplate<String, String> kafkaConsumerTemplate;

    @PostMapping("/checkout")
    public Mono<BaseResponse<BookingCreateResponse>> createBooking(@RequestBody BookingCreateRequest bookingCreateRequest) {
        String bookingCode = Utils.generateBookingCode();
        bookingCreateRequest.setBookingCode(bookingCode);
        bookingCreateRequest.setUsername(jwtUtils.getUserNameFromJwtToken());

        kafkaSendService.send(Constants.BOOKING_HANDLER_TOPIC, bookingCreateRequest);
        KafkaReceiverSingletonBooking kafkaReceiverSingletonBooking = KafkaReceiverSingletonBooking.getInstance(kafkaConsumerTemplate);
        return kafkaReceiverSingletonBooking.getKafkaFlux()
                .checkpoint("Starting kafka message streaming.")
                .log()
                .doOnNext(consumerRecord -> log.info("received key={}, value={} from topic={}, offset={}",
                        consumerRecord.key(),
                        consumerRecord.value(),
                        consumerRecord.topic(),
                        consumerRecord.offset())
                )
                .map(record -> Utils.readObjectFromJsonFormat(record.value(), BookingCreateResponse.class))
                .filter(response -> response.getBookingCode().equals(bookingCreateRequest.getBookingCode()))
                .map(r -> BaseResponse.ofSucceeded(r))
                .take(1)
                .single();
    }

    @PostMapping("/make-payment")
    public ResponseEntity<?> makePayment(@RequestBody BookingPaymentRequest bookingPaymentRequest) {
        Object response = paymentService.createPayment(bookingPaymentRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete-payment")
    public ResponseEntity<BaseResponse> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId) {
        paymentService.completePayment(paymentId, payerId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping("/cancel-booking")
    public ResponseEntity<BaseResponse> cancelBooking(@RequestParam("bookingId") Integer bookingId) {
        bookingService.cancelBooking(bookingId, true);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

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
