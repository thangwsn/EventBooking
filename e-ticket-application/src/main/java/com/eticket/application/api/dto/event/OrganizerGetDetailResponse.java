package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizerGetDetailResponse {
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String mobile;
    private String representative;
    private String taxCode;
    private String summary;
    private String updateBy; // username
    private List<EventGetResponse> listEvent;
    private double sales;
}
