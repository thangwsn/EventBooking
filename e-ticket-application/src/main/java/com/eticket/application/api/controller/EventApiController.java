package com.eticket.application.api.controller;

import com.eticket.application.api.dto.event.EventCreateRequest;
import com.eticket.application.api.dto.event.EventUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/emp/event")
@RequiredArgsConstructor
public class EventApiController {
    @PostMapping
    public ResponseEntity<?> addNewEvent(@RequestBody EventCreateRequest eventCreateRequest) {
        return null;
    }

    @PutMapping("/{event_id}")
    public ResponseEntity<?> updateEvent(@PathVariable("event_id") Integer eventId, @RequestBody EventUpdateRequest eventUpdateRequest) {
        return null;
    }

    @DeleteMapping("/{event_id}")
    public ResponseEntity<?> removeEvent(@PathVariable("event_id") Integer eventId) {
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllEvent() {
        return null;
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<?> getEvent(@PathVariable("event_id") Integer eventId) {
        return null;
    }
}
