package com.eticket.application.websocket;

import com.eticket.application.api.dto.event.EventWebSocketDto;
import com.eticket.application.websocket.observer.Notification;
import com.eticket.application.websocket.observer.Observer;
import com.eticket.domain.service.EventServiceImpl;
import com.eticket.infrastructure.utils.Utils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventWSController implements Observer<EventServiceImpl> {
    private final SimpMessagingTemplate simpMessagingTemplate;
    EventServiceImpl eventService;

    public EventWSController(SimpMessagingTemplate simpMessagingTemplate, EventServiceImpl eventService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.eventService = eventService;
        this.eventService.subscribe(this);
    }

    @Scheduled(fixedDelay = 3000)
    void sendEventDetailInterval() {
        List<EventWebSocketDto> eventList = eventService.getAllEventForWS();
        eventList.forEach(e -> {
            System.out.println(Utils.convertObjectToJson(e));
            this.simpMessagingTemplate.convertAndSendToUser(e.getId().toString(), "/event-update", Utils.convertObjectToJson(e));
        });
    }

    @Override
    public void handle(Notification<EventServiceImpl> notification) {
        String notificationJson = Utils.convertObjectToJson(notification);
        this.simpMessagingTemplate.convertAndSendToUser(notification.eventId.toString(), "/notify/event", notificationJson);
    }
}
