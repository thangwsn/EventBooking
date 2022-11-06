package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.event.EventDetailGetResponse;
import com.eticket.application.api.dto.event.EventGetRequest;
import com.eticket.application.api.dto.event.ListEventGetResponse;
import com.eticket.domain.service.EventService;
import com.eticket.infrastructure.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/event")
@EnableAsync
public class EventClientApiController {
    @Autowired
    private EventService eventService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/get-events")
    public ResponseEntity<BaseResponse<ListEventGetResponse>> getEvents(@RequestBody EventGetRequest request) {
        ListEventGetResponse response = eventService.getAllEventForUserSide(request);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<BaseResponse<EventDetailGetResponse>> getEvent(@PathVariable("event_id") Integer eventId) {
        EventDetailGetResponse response = eventService.getEventByIdFromClientSide(eventId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/{event_id}/toggle-follow")
    public ResponseEntity<BaseResponse<Void>> toggleFollow(@PathVariable("event_id") Integer eventId) {
        eventService.toggleFollow(eventId, jwtUtils.getUserNameFromJwtToken());
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }
}
