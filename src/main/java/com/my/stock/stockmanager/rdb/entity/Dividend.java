package com.my.stock.stockmanager.rdb.entity;

import com.my.stock.stockmanager.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dividend extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private int year;

    private int month;

    private int day;

    private String symbol;

    @Column(nullable = false, precision = 24, scale = 6)
    private BigDecimal dividend;

}
