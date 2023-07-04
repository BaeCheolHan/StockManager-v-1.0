package com.my.stock.stockmanager.dto.bank.account;

import com.my.stock.stockmanager.constants.Bank;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {
	private Long id;
	private String memo;
	private Bank bank;

	public BankAccountDto(BankAccount bankAccount) {
		this.id = bankAccount.getId();
		this.memo = bankAccount.getMemo();
		this.bank = bankAccount.getBank();
	}
}
