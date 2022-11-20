package com.eticket.application.websocket;

import com.eticket.application.websocket.observer.Notification;
import com.eticket.application.websocket.observer.Observer;
import com.eticket.domain.service.BookingServiceImpl;
import com.eticket.infrastructure.utils.Utils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BookingWSController implements Observer<BookingServiceImpl> {
    private final SimpMessagingTemplate simpMessagingTemplate;
    BookingServiceImpl bookingService;

    public BookingWSController(SimpMessagingTemplate simpMessagingTemplate, BookingServiceImpl bookingService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.bookingService = bookingService;
        this.bookingService.subscribe(this);
    }

    @Override
    public void handle(Notification notification) {
        String notificationJson = Utils.convertObjectToJson(notification);
        System.out.println(notificationJson);
        this.simpMessagingTemplate.convertAndSendToUser(notification.eventId.toString(), "/notify", notificationJson);
    }
}
