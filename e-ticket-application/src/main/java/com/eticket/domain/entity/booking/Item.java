package com.eticket.domain.entity.booking;

import com.eticket.domain.entity.event.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "quantity")
    private Integer quantity;
    @ManyToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne(targetEntity = Ticket.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
