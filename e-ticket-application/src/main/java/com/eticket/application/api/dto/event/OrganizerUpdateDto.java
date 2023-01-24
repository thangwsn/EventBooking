package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizerUpdateDto {
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String mobile;
    private String representative;
    private String taxCode;
    private String summary;
}
