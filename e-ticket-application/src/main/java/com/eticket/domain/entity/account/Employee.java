package com.eticket.domain.entity.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee extends Account {
    @Column(name = "employee_code", unique = true)
    private String employeeCode;
    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private Position position;
    @Column(name = "join_date")
    @Temporal(TemporalType.DATE)
    private Date joinDate;
}
