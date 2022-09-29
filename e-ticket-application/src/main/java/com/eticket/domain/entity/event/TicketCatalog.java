package com.eticket.domain.entity.event;

import com.eticket.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket_catalog")
public class TicketCatalog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "slot")
    private Integer slot;
    @Column(name = "sold_slot")
    private Integer soldSlot;
    @Column(name = "price")
    private Double price;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
