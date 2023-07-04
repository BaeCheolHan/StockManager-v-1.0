package com.my.stock.stockmanager.rdb.dto.request;

import com.my.stock.stockmanager.rdb.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BankAccountSaveRequest {
	private Long memberId;
	private BankAccount bankAccount;
}
