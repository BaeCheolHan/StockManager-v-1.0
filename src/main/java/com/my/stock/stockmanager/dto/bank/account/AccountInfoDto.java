package com.my.stock.stockmanager.dto.bank.account;

import com.my.stock.stockmanager.constants.Bank;
import com.my.stock.stockmanager.rdb.entity.Stock;

import java.util.List;

public class AccountInfoDto {
	private Long id;
	private String alias;
	private Bank bank;

	private BankEnumMapperValue bankValue;
	private List<Stock> stocks;
}
