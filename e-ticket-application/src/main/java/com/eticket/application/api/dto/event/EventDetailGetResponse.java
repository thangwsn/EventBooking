package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailGetResponse {
    private Integer id;
    private String title;
    private String typeString;
    private String summary;
    private String description;
    private String videoLink;
    private String[] listTag;
    private String statusString;
    private long startTime;
    private long launchTime;
    private long closeTime;
    private double duration;
    private double totalSlot;
    private double sales;
    private int soldSlot;
    private int remainSlot;
    private int followerNum;
    private OrganizerGetResponse organizer;
    private String locationString;
    private List<String> imagePathList;
    private List<TicketCatalogGetResponse> ticketCatalogList;
    private boolean followed;
    private boolean disableBooking;
}
