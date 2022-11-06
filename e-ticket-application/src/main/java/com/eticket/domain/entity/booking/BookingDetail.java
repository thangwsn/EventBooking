package com.eticket.domain.entity.booking;

import com.eticket.domain.entity.event.TicketCatalog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking_detail")
@Builder
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(targetEntity = Booking.class)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne(targetEntity = TicketCatalog.class)
    @JoinColumn(name = "ticket_catalog_id")
    private TicketCatalog ticketCatalog;
    @Column(name = "quantity")
    private int quantity;
}
