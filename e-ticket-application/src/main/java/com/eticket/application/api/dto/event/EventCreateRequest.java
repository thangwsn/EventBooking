package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class EventCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String typeString;
    @NotBlank
    private String summary;
    @NotBlank
    private String description;
    private String videoLink;
    @NotBlank
    private String listTag;
    @NotBlank
    private long startTimeMs;
    @NotBlank
    private long launchTimeMs;
    @NotBlank
    private long closeTimeMs;
    @NotBlank
    private double duration;
    @NotBlank
    private double totalSlot;
    @NotBlank
    private int organizerId;
    private LocationDto locationDto;
}
