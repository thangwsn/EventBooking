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
    private int slot;
    @Column(name = "sold_slot")
    private int soldSlot;
    @Column(name = "remain_slot")
    private int remainSlot;
    @Column(name = "price")
    private double price;
    @Column(name = "remark")
    private String remark;
    @Column(name = "removed")
    private boolean removed;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Override
    public String toString() {
        return "TicketCatalog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slot=" + slot +
                ", soldSlot=" + soldSlot +
                ", remainSlot=" + remainSlot +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                ", removed=" + removed +
                ", event=" + event.getId() +
                '}';
    }
}
