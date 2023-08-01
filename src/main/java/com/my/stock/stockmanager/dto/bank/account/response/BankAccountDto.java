package com.my.stock.stockmanager.dto.bank.account.response;

import com.my.stock.stockmanager.constants.Bank;
import com.my.stock.stockmanager.dto.bank.account.BankEnumMapperValue;
import com.my.stock.stockmanager.dto.personal.setting.PersonalBankAccountSettingDto;
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

	private String alias;
	private Bank bank;

	private BankEnumMapperValue bankInfo;

	private PersonalBankAccountSettingDto personalBankAccountSetting;

	public BankAccountDto(BankAccount bankAccount) {
		this.id = bankAccount.getId();
		this.alias = bankAccount.getAlias();
		this.memo = bankAccount.getMemo();
		this.bank = bankAccount.getBank();

		if (this.bank != null) {
			this.bankInfo = new BankEnumMapperValue(this.bank);
		}

		this.personalBankAccountSetting = new PersonalBankAccountSettingDto(bankAccount.getPersonalBankAccountSetting());

	}
}
