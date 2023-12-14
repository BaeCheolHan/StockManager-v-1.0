package com.my.stock.stockmanager.rdb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class MyStockSnapShot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String symbol;
	@Column(nullable = false, precision = 24, scale = 6)
	private BigDecimal quantity;
	@Column(nullable = false, precision = 24, scale = 6)
	private BigDecimal averPrice;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private BankAccount bankAccount;

}
