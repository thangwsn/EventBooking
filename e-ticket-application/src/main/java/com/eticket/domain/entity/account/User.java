package com.eticket.domain.entity.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends Account {
    @Column(name = "amount_reserved")
    private double amountReserved;
    @Column(name = "user_code", unique = true)
    private String userCode;

}
