package com.eticket.domain.entity.booking;

import com.eticket.domain.entity.account.User;
import com.eticket.domain.entity.event.Event;
import com.eticket.domain.entity.event.Ticket;
import com.eticket.infrastructure.utils.Constants;
import com.eticket.infrastructure.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "amount")
    private double amount;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @Column(name = "removed")
    private boolean removed;
    @Column(name = "fullName")
    private String fullName;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "booking_timeout")
    private Timestamp bookingTimeout;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<Ticket> listTicket;
    @ManyToOne(targetEntity = Event.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, updatable = false)
    @CreatedDate
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = true)
    @LastModifiedDate
    private Date updateTime;

    @PrePersist
    public void setCreateTime() {
        createTime = new Date();
        updateTime = new Date();
        bookingTimeout = Utils.convertToTimeStamp(new Date().getTime() + Constants.BOOKING_TIMEOUT);
    }
}
