package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.FieldViolation;
import com.eticket.application.api.dto.event.*;
import com.eticket.domain.exception.AuthenticationException;
import com.eticket.domain.exception.EventRemoveException;
import com.eticket.domain.exception.ResourceNotFoundException;
import com.eticket.domain.exception.TicketCatalogException;
import com.eticket.domain.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/emp/event")
public class EventApiController {
    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> addNewEvent(@RequestParam("files") MultipartFile[] files, @RequestParam("data") String data) throws AuthenticationException, ResourceNotFoundException {
        eventService.registerEvent(files, data);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PutMapping("/{event_id}")
    public ResponseEntity<BaseResponse> updateEvent(@PathVariable("event_id") Integer eventId, @RequestBody EventUpdateRequest eventUpdateRequest) throws ResourceNotFoundException {
        List<FieldViolation> violationList = eventService.updateEvent(eventId, eventUpdateRequest);
        if (violationList.size() > 0) {
            return ResponseEntity.ok(BaseResponse.ofInvalid(violationList));
        }
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @DeleteMapping("/{event_id}")
    public ResponseEntity<?> removeEvent(@PathVariable("event_id") Integer eventId) throws EventRemoveException, ResourceNotFoundException {
        eventService.removeEvent(eventId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<ListEventGetResponse>> getAllEvent() {
        ListEventGetResponse response = eventService.getAllEvent();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<BaseResponse<EventDetailGetResponse>> getEvent(@PathVariable("event_id") Integer eventId) throws ResourceNotFoundException {
        EventDetailGetResponse response = eventService.getEventById(eventId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @PostMapping("/{event_id}/ticket-catalog")
    public ResponseEntity<BaseResponse<Void>> addTicketCatalog(@PathVariable("event_id") Integer eventId, @RequestBody TicketCatalogRequest ticketCatalogRequest) throws ResourceNotFoundException {
        eventService.addTicketCatalog(eventId, ticketCatalogRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PutMapping("/{event_id}/ticket-catalog/{ticket_catalog_id}")
    public ResponseEntity<BaseResponse<Void>> editTicketCatalog(@PathVariable("ticket_catalog_id") Integer ticketCatalogId, @RequestBody TicketCatalogUpdateRequest ticketCatalogUpdateRequest) throws TicketCatalogException, ResourceNotFoundException {
        eventService.updateTicketCatalog(ticketCatalogId, ticketCatalogUpdateRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @DeleteMapping("/{event_id}/ticket-catalog/{ticket_catalog_id}")
    public ResponseEntity<BaseResponse<Void>> removeTicketCatalog(@PathVariable("ticket_catalog_id") Integer ticketCatalogId) throws TicketCatalogException, ResourceNotFoundException {
        eventService.removeTicketCatalog(ticketCatalogId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PostMapping("/change-status")
    public ResponseEntity<BaseResponse<Void>> changeEventStatus(@RequestBody ChangeEventStatusRequest changeEventStatusRequest) throws ResourceNotFoundException {
        eventService.changeEventStatus(changeEventStatusRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping("/type")
    public ResponseEntity getListEventType() {
        List<String> response = eventService.getListEventType();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{event_id}")
    public ResponseEntity getListEventStatus(@PathVariable("event_id") Integer eventId) throws ResourceNotFoundException {
        List<String> response = eventService.getListEventStatus(eventId);
        return ResponseEntity.ok(response);
    }
}
