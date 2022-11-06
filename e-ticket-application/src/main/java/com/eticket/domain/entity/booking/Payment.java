package com.eticket.domain.entity.booking;

import com.eticket.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
@Builder
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "amount")
    private double amount;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PaymentType type;
    @OneToOne(targetEntity = Booking.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
