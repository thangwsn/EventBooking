package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEventStatusRequest {
    private Integer eventId;
    private String targetStatus;
}
