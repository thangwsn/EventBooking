package com.eticket.domain.service;

import com.eticket.application.api.dto.event.*;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.EventRemoveException;
import com.eticket.domain.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    void registerEvent(MultipartFile[] files, String data) throws AuthenticationException, ResourceNotFoundException;

    void removeEvent(Integer eventId) throws ResourceNotFoundException, EventRemoveException;

    void addTicketCatalog(Integer eventId, TicketCatalogRequest listTicketCatalogRequest) throws ResourceNotFoundException;

    ListEventGetResponse getAllEvent();

    EventDetailGetResponse getEventById(Integer eventId) throws ResourceNotFoundException;

    EventDetailGetResponse getEventByIdFromClientSide(Integer eventId) throws ResourceNotFoundException;

    ListEventGetResponse getAllEventForUserSide(EventGetRequest request);

    void toggleFollow(Integer eventId, String username);

    List<String> getListEventType();

    List<String> getListEventStatus(Integer eventId) throws ResourceNotFoundException;

    void changeEventStatus(ChangeEventStatusRequest changeEventStatusRequest) throws ResourceNotFoundException;

    List<EventWebSocketDto> getAllEventForWS();

}
