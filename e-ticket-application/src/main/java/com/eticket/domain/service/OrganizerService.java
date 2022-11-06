package com.eticket.domain.service;

import com.eticket.application.api.dto.event.ListOrganizerGetResponse;
import com.eticket.application.api.dto.event.OrganizerCreateRequest;
import com.eticket.application.api.dto.event.OrganizerGetDetailResponse;

public interface OrganizerService {
    boolean registerOrganizer(OrganizerCreateRequest organizerCreateRequest);

    void removeOrganizer(Integer organizerId);

    ListOrganizerGetResponse getAllOrganizer();

    OrganizerGetDetailResponse getOrganizerById(Integer organizerId);
}
