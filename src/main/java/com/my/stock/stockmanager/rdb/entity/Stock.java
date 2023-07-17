package com.my.stock.stockmanager.rdb.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Stock extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String symbol;
	@Column(nullable = false)
	private BigDecimal quantity;
	@Column(nullable = false)
	private BigDecimal price;

	@JsonBackReference
	@ManyToOne
	private BankAccount bankAccount;

}
