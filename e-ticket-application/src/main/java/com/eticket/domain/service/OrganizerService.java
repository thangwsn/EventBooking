package com.eticket.domain.service;

import com.eticket.application.api.dto.event.ListOrganizerGetResponse;
import com.eticket.application.api.dto.event.OrganizerCreateRequest;
import com.eticket.application.api.dto.event.OrganizerGetDetailResponse;
import com.eticket.application.api.dto.event.OrganizerUpdateDto;
import com.eticket.domain.exception.ResourceNotFoundException;

public interface OrganizerService {
    boolean registerOrganizer(OrganizerCreateRequest organizerCreateDto) throws ResourceNotFoundException;

    void removeOrganizer(Integer organizerId);

    ListOrganizerGetResponse getAllOrganizer();

    OrganizerGetDetailResponse getOrganizerById(Integer organizerId);

    OrganizerUpdateDto getOrganizerForEdit(Integer organizerId) throws ResourceNotFoundException;

    void updateOrganizer(Integer organizerId, OrganizerUpdateDto organizerUpdateRequest) throws ResourceNotFoundException;
}
