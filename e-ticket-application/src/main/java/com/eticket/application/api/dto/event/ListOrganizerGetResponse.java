package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOrganizerGetResponse {
    private int size;
    private List<OrganizerGetResponse> listOrganizer;
}
