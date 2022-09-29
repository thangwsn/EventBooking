package com.eticket.application.api.controller;

import com.eticket.application.api.dto.event.OrganizerCreateRequest;
import com.eticket.application.api.dto.event.OrganizerUpdateRequest;
import com.eticket.domain.service.OrganizerSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/organizer")
@RequiredArgsConstructor
public class OrganizerApiController {
    private OrganizerSevice organizerSevice;

    @PostMapping
    public ResponseEntity<?> addNewOrganizer(@Valid @RequestBody OrganizerCreateRequest organizerCreateRequest) {
        return null;
    }

    @PutMapping("/{organizer_id}")
    public ResponseEntity<?> updateOrganizer(@PathVariable("organzier_id") Integer organizerId, @Valid @RequestBody OrganizerUpdateRequest organizerUpdateRequest) {
        return null;
    }

    @DeleteMapping("/{organizer_id}")
    public ResponseEntity<?> removeOrganizer(@PathVariable("organizer_id") Integer organizerId) {
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrganizer() {
        return null;
    }

    @GetMapping("/{organizer_id}")
    public ResponseEntity<?> getOrganizer(@PathVariable("organizer_id") Integer organizerId) {
        return null;
    }
}
