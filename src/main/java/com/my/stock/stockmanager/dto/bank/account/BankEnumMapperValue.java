package com.my.stock.stockmanager.dto.bank.account;

import com.my.stock.stockmanager.constants.BankEnumMapperType;
import lombok.Getter;

@Getter
public class BankEnumMapperValue {
	private final String code;
	private final String bankCode;
	private final String bankName;

	public BankEnumMapperValue(BankEnumMapperType bankType) {
		this.code = bankType.getCode();
		this.bankCode = bankType.getBankCode();
		this.bankName = bankType.getBankName();
	}

	@Override
	public String toString() {
		return "{" +
				"code='" + bankCode + "'" +
				", bankName='" + bankName + "'"
				+ "}";
	}
}
