package com.my.stock.stockmanager.rdb.entity;

import com.my.stock.stockmanager.base.response.entity.BaseTimeEntity;
import com.my.stock.stockmanager.constants.Bank;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode
@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String memo;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Bank bank;

	@ManyToOne
	private Member member;

	@OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY)
	private List<Stock> stocks;

	@OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY)
	private List<DepositWithdrawalHistory> depositWithdrawalHistories;
}
