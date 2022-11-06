package com.eticket.infrastructure.mapper;

import com.eticket.application.api.dto.event.OrganizerGetResponse;
import com.eticket.domain.entity.event.Organizer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizerMap {
    @Autowired
    private ModelMapper modelMapper;
    public OrganizerGetResponse toOrganizerGetResponse(Organizer organizer) {
        OrganizerGetResponse organizerGetResponse = modelMapper.map(organizer, OrganizerGetResponse.class);
        organizerGetResponse.setUpdateBy(organizer.getUpdateByEmployee().getUsername());
        return organizerGetResponse;
    }
}
