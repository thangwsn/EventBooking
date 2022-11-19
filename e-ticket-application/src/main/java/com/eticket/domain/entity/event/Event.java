package com.eticket.domain.entity.event;

import com.eticket.domain.entity.BaseEntity;
import com.eticket.domain.entity.account.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event")
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EventType type;
    @Column(name = "summary")
    private String summary;
    @Column(name = "description")
    private String description;
    @Column(name = "videoLink")
    private String videoLink;
    @Column(name = "listTag")
    private String listTag;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "launch_time")
    private Timestamp launchTime;
    @Column(name = "close_time")
    private Timestamp closeTime;
    @Column(name = "duration")
    private double duration;
    @Column(name = "total_slot")
    private int totalSlot;
    @Column(name = "sales")
    private double sales;
    @Column(name = "sold_slot")
    private int soldSlot;
    @Column(name = "remain_slot")
    private int remainSlot;
    @Column(name = "removed")
    private boolean removed;
    @ManyToOne(targetEntity = Organizer.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;
    @OneToOne(targetEntity = Location.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Image> imageList;
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<TicketCatalog> ticketCatalogList;
    @ManyToOne(targetEntity = Employee.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by")
    private Employee updateBy;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                '}';
    }

    public String locationToString() {
        return String.format("%s, %s, %s, %s", location.getStreet(), location.getWard(), location.getDistrict(), location.getCity());
    }
}
