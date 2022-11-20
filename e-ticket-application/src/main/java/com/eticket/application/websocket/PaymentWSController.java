package com.eticket.application.websocket;

import com.eticket.application.websocket.observer.Notification;
import com.eticket.application.websocket.observer.Observer;
import com.eticket.domain.service.PaymentServiceImpl;
import com.eticket.infrastructure.utils.Utils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PaymentWSController implements Observer<PaymentServiceImpl> {
    private final SimpMessagingTemplate simpMessagingTemplate;
    PaymentServiceImpl paymentService;

    public PaymentWSController(SimpMessagingTemplate simpMessagingTemplate, PaymentServiceImpl paymentService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.paymentService = paymentService;
        this.paymentService.subscribe(this);
    }

    @Override
    public void handle(Notification notification) {
        String notificationJson = Utils.convertObjectToJson(notification);
        System.out.println(notificationJson);
        this.simpMessagingTemplate.convertAndSendToUser(notification.eventId.toString(), "/notify", notificationJson);
    }
}
