package com.eticket.application.api.controller;

import com.eticket.application.api.dto.BaseResponse;
import com.eticket.application.api.dto.event.ListOrganizerGetResponse;
import com.eticket.application.api.dto.event.OrganizerCreateRequest;
import com.eticket.application.api.dto.event.OrganizerGetDetailResponse;
import com.eticket.application.api.dto.event.OrganizerUpdateDto;
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
    private OrganizerService organizerService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> addNewOrganizer(@Valid @RequestBody OrganizerCreateRequest organizerCreateDto) throws ResourceNotFoundException {
        boolean accept = organizerService.registerOrganizer(organizerCreateDto);
        if (!accept) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @PutMapping("/edit/{organizer_id}")
    public ResponseEntity<BaseResponse<Void>> editOrganizer(@PathVariable("organizer_id") Integer organizerId, @Valid @RequestBody OrganizerUpdateDto organizerUpdateRequest) throws ResourceNotFoundException {
        organizerService.updateOrganizer(organizerId, organizerUpdateRequest);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping("/edit/{organizer_id}")
    public ResponseEntity<BaseResponse<OrganizerUpdateDto>> getOrganizerForEdit(@PathVariable("organizer_id") Integer organizerId) throws ResourceNotFoundException {
        OrganizerUpdateDto response = organizerService.getOrganizerForEdit(organizerId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @DeleteMapping("/{organizer_id}")
    public ResponseEntity<BaseResponse<Void>> removeOrganizer(@PathVariable("organizer_id") Integer organizerId) {
        organizerService.removeOrganizer(organizerId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<ListOrganizerGetResponse>> getAllOrganizer() {
        ListOrganizerGetResponse response = organizerService.getAllOrganizer();
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }

    @GetMapping("/{organizer_id}")
    public ResponseEntity<BaseResponse<OrganizerGetDetailResponse>> getOrganizer(@PathVariable("organizer_id") Integer organizerId) {
        OrganizerGetDetailResponse response = organizerService.getOrganizerById(organizerId);
        return ResponseEntity.ok(BaseResponse.ofSucceeded(response));
    }
}
