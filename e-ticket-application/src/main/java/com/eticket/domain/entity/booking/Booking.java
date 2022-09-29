package com.eticket.domain.entity.booking;

import com.eticket.domain.entity.BaseEntity;
import com.eticket.domain.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer ID;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @Column(name = "removed")
    private boolean removed;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private List<Item> itemList;

}
