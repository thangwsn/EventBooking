package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizerGetResponse {
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String mobile;
    private String representative;
    private String taxCode;
    private String updateBy; // username
    private String summary;
}
