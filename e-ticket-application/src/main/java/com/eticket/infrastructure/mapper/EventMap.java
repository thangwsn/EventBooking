package com.eticket.infrastructure.mapper;

import com.eticket.application.api.dto.event.*;
import com.eticket.domain.entity.event.Event;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMap {
    @Autowired
    private ModelMapper modelMapper;

    public EventGetResponse toEventGetResponse(Event event, int followNum, boolean userView) {
        EventGetResponse eventGetResponse = modelMapper.map(event, EventGetResponse.class);
        eventGetResponse.setFollowerNum(followNum);
        eventGetResponse.setStatusString(event.getStatus().name());
        eventGetResponse.setTypeString(event.getType().name());
        eventGetResponse.setLocationString(event.locationToString());
        eventGetResponse.setOrganizer(OrganizerGetResponse.builder().id(event.getOrganizer().getId()).name(event.getOrganizer().getName()).build());
        List<String> imagePathList = event.getImageList()
                .stream().map(image -> image.getUrl()).collect(Collectors.toList());
        eventGetResponse.setImagePathList(imagePathList);
        if (userView) {
            eventGetResponse.setSales(0.0);
        }
        return eventGetResponse;
    }

    public EventDetailGetResponse toEventDetailGetResponse(Event event, int followNum, boolean userView) {
        EventDetailGetResponse eventDetailGetResponse = modelMapper.map(event, EventDetailGetResponse.class);
        eventDetailGetResponse.setFollowerNum(followNum);
        eventDetailGetResponse.setStatusString(event.getStatus().name());
        eventDetailGetResponse.setTypeString(event.getType().name());
        eventDetailGetResponse.setLocationString(event.locationToString());
        eventDetailGetResponse.setOrganizer(OrganizerGetResponse.builder().id(event.getOrganizer().getId()).name(event.getOrganizer().getName()).build());
        List<String> imagePathList = event.getImageList()
                .stream().map(image -> image.getUrl()).collect(Collectors.toList());
        eventDetailGetResponse.setImagePathList(imagePathList);
        List<TicketCatalogGetResponse> catalogGetResponseList = event.getTicketCatalogList()
                .stream().map(ticketCatalog -> modelMapper.map(ticketCatalog, TicketCatalogGetResponse.class))
                .collect(Collectors.toList());
        eventDetailGetResponse.setTicketCatalogList(catalogGetResponseList);
        if (userView) {
            eventDetailGetResponse.setSales(0.0);
        }
        eventDetailGetResponse.setFollowed(false);
        eventDetailGetResponse.setDisableBooking(false);
        return eventDetailGetResponse;
    }

    public EventWebSocketDto toEventWebSocketDto(Event event, int followNum) {
        EventWebSocketDto eventWebSocketDto = modelMapper.map(event, EventWebSocketDto.class);
        eventWebSocketDto.setStatusString(event.getStatus().name());
        List<TicketCatalogGetResponse> catalogGetResponseList = event.getTicketCatalogList()
                .stream().map(ticketCatalog -> modelMapper.map(ticketCatalog, TicketCatalogGetResponse.class))
                .collect(Collectors.toList());
        eventWebSocketDto.setTicketCatalogList(catalogGetResponseList);
        eventWebSocketDto.setFollowerNum(followNum);
        return eventWebSocketDto;
    }
}
