package com.eticket.domain.entity.event;

import com.eticket.domain.entity.BaseEntity;
import com.eticket.domain.entity.account.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organizer")
public class Organizer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "mobile", nullable = false)
    private String mobile;
    @Column(name = "representative")
    private String representative;
    @Column(name = "tax_code")
    private String taxCode;
    @Column(name = "summary")
    private String summary;
    @Column(name = "removed")
    private boolean removed;
    @ManyToOne(targetEntity = Employee.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by")
    private Employee updateByEmployee;
}
