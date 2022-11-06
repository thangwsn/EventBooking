package com.eticket.domain.entity.event;

import com.eticket.domain.entity.BaseEntity;
import com.eticket.domain.entity.account.User;
import com.eticket.domain.entity.booking.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket")
@Builder
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "price")
    private double price;
    @Column(name = "qr_code")
    private String QRcode;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Column(name = "sold_time")
    private Timestamp soldTime;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(targetEntity = TicketCatalog.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_catalog_id")
    private TicketCatalog ticketCatalog;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
    private Booking booking;
}
