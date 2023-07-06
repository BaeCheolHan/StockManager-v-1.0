package com.my.stock.stockmanager.dto.bank.account.request;

import com.my.stock.stockmanager.constants.Bank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BankAccountSaveRequest {
	private Long memberId;
	private String alias;
	private Bank bank;
}
