package com.eticket.application.api.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventGetResponse {
    private Integer id;
    private String title;
    private String typeString;
    private String summary;
    private List<String> listTag;
    private String statusString;
    private double duration;
    private double sales;
    private int totalSlot;
    private int soldSlot;
    private int remainSlot;
    private int followerNum;
    private OrganizerGetResponse organizer;
    private String locationString;
    private List<String> imagePathList;
}
