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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@JsonManagedReference
	@OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Stock> stocks;

	@JsonManagedReference
	@OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MyStockSnapShot> myStockSnapShot;

	@JsonManagedReference
	@OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DepositWithdrawalHistory> depositWithdrawalHistories;

	@OneToOne(mappedBy = "bankAccount", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private PersonalBankAccountSetting personalBankAccountSetting;
}
