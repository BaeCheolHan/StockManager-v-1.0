package com.my.stock.stockmanager.rdb.entity;

import com.my.stock.stockmanager.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositWithdrawalHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount;

    private String memo;

    @ManyToOne
    private BankAccount bankAccount;

}
