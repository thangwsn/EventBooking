package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.event.ListOrganizerGetResponse;
import com.eticket.application.api.dto.event.OrganizerCreateRequest;
import com.eticket.application.api.dto.event.OrganizerGetDetailResponse;
import com.eticket.application.api.dto.event.OrganizerUpdateRequest;
import com.eticket.domain.exception.ResourceNotFoundException;
import com.eticket.domain.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/organizer")
public class OrganizerApiController {
    @Autowired
    private OrganizerService organizerSevice;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> addNewOrganizer(@Valid @RequestBody OrganizerCreateRequest organizerCreateRequest) throws ResourceNotFoundException {
        boolean accept = organizerSevice.registerOrganizer(organizerCreateRequest);
        if (!accept) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PutMapping("/{organizer_id}")
    public ResponseEntity<?> updateOrganizer(@PathVariable("organzier_id") Integer organizerId, @Valid @RequestBody OrganizerUpdateRequest organizerUpdateRequest) {
        return null;
    }

    @DeleteMapping("/{organizer_id}")
    public ResponseEntity<BaseResponse<Void>> removeOrganizer(@PathVariable("organizer_id") Integer organizerId) {
        organizerSevice.removeOrganizer(organizerId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<ListOrganizerGetResponse>> getAllOrganizer() {
        ListOrganizerGetResponse response = organizerSevice.getAllOrganizer();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/{organizer_id}")
    public ResponseEntity<BaseResponse<OrganizerGetDetailResponse>> getOrganizer(@PathVariable("organizer_id") Integer organizerId) {
        OrganizerGetDetailResponse response = organizerSevice.getOrganizerById(organizerId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }
}
