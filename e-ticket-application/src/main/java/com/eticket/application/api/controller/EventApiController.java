package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.event.*;
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
    public ResponseEntity<BaseResponse<Void>> addNewEvent(@RequestParam("files") MultipartFile[] files, @RequestParam("data") String data) {
        eventService.registerEvent(files, data);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PutMapping("/{event_id}")
    public ResponseEntity<?> updateEvent(@PathVariable("event_id") Integer eventId, @RequestBody EventUpdateRequest eventUpdateRequest) {
        return null;
    }

    @DeleteMapping("/{event_id}")
    public ResponseEntity<?> removeEvent(@PathVariable("event_id") Integer eventId) {
        eventService.removeEvent(eventId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<ListEventGetResponse>> getAllEvent() {
        ListEventGetResponse response = eventService.getAllEvent();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<BaseResponse<EventDetailGetResponse>> getEvent(@PathVariable("event_id") Integer eventId) {
        EventDetailGetResponse response = eventService.getEventById(eventId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @PostMapping("/{event_id}/ticket-catalog")
    public ResponseEntity<BaseResponse<TicketCatalogGetResponse>> addTicketCatalog(@PathVariable("event_id") Integer eventId, @RequestBody TicketCatalogRequest ticketCatalogRequest) {
        eventService.addTicketCatalog(eventId, ticketCatalogRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PutMapping("/{event_id}/ticket-catalog")
    public ResponseEntity<?> editTicketCatalog(@PathVariable("event_id") Integer eventId, @RequestBody TicketCatalogUpdateRequest ticketCatalogUpdateRequest) {
        return null;
    }

    @PostMapping("/change-status")
    public ResponseEntity<BaseResponse<Void>> changeEventStatus(@RequestBody ChangeEventStatusRequest changeEventStatusRequest) {
        eventService.changeEventStatus(changeEventStatusRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping("/type")
    public ResponseEntity getListEventType() {
        List<String> response = eventService.getListEventType();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{event_id}")
    public ResponseEntity getListEventStatus(@PathVariable("event_id") Integer eventId) {
        List<String> response = eventService.getListEventStatus(eventId);
        return ResponseEntity.ok(response);
    }
}
