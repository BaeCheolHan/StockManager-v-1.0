package com.my.stock.stockmanager.rdb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.my.stock.stockmanager.base.entity.BaseTimeEntity;
import com.my.stock.stockmanager.constants.SnsType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


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

    private BigDecimal dividend;


}
