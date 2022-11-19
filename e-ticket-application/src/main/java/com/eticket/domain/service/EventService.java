package com.eticket.domain.service;

import com.eticket.application.api.dto.event.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    void registerEvent(MultipartFile[] files, String data);

    void removeEvent(Integer eventId);

    void addTicketCatalog(Integer eventId, TicketCatalogRequest listTicketCatalogRequest);

    ListEventGetResponse getAllEvent();

    EventDetailGetResponse getEventById(Integer eventId);

    EventDetailGetResponse getEventByIdFromClientSide(Integer eventId);

    ListEventGetResponse getAllEventForUserSide(EventGetRequest request);

    void toggleFollow(Integer eventId, String username);

    List<String> getListEventType();

    List<String> getListEventStatus(Integer eventId);

    void changeEventStatus(ChangeEventStatusRequest changeEventStatusRequest);

    List<EventWebSocketDto> getAllEventForWS();

}
