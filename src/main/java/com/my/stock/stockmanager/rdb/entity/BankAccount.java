package com.my.stock.stockmanager.rdb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.my.stock.stockmanager.base.entity.BaseTimeEntity;
import com.my.stock.stockmanager.constants.Bank;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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

	private String alias;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Bank bank;

	@JsonBackReference
	@ManyToOne
	private Member member;

	@JsonManagedReference
	@OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY)
	private List<Stock> stocks;

	@JsonManagedReference
	@OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY)
	private List<DepositWithdrawalHistory> depositWithdrawalHistories;
}
