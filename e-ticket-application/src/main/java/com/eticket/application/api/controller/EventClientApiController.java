package com.eticket.application.api.controller;

import com.eticket.application.api.dto.event.EventClientGetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventClientApiController {
    @PostMapping("/get-all")
    public RequestEntity<?> getAllEvent(@RequestBody EventClientGetRequest eventClientGetRequest) {
        return null;
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<?> getEvent(@PathVariable("event_id") Integer eventId) {
        return null;
    }

    @GetMapping(value = "/listen", consumes = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> listenListEventUpdate() {
        return null;
    }

    @GetMapping(value = "/listen/{event_id}", consumes = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> listenEventUpdate(@PathVariable("event_id") Integer eventId) {
        return null;
    }
}
