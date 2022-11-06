package com.eticket.infrastructure.mapper;

import com.eticket.application.api.dto.booking.TicketGetResponse;
import com.eticket.domain.entity.event.Ticket;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketMap {
    @Autowired
    private ModelMapper modelMapper;

    public TicketGetResponse toTicketGet(Ticket ticket) {
        TicketGetResponse ticketGetResponse = modelMapper.map(ticket, TicketGetResponse.class);
        ticketGetResponse.setSoldTime(ticket.getSoldTime().getTime());
        ticketGetResponse.setTicketCatalogTitle(ticket.getTicketCatalog().getTitle());
        ticketGetResponse.setEventTitle(ticket.getEvent().getTitle());
        ticketGetResponse.setStartTime(ticket.getEvent().getStartTime().getTime());
        ticketGetResponse.setLocationString(ticket.getEvent().locationToString());
        return ticketGetResponse;
    }
}
