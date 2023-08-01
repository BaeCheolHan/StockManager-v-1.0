package com.my.stock.stockmanager.dto.personal.setting;

import com.my.stock.stockmanager.rdb.entity.PersonalBankAccountSetting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalBankAccountSettingDto {
	private Long id;
	private String defaultNational;
	private String defaultCode;

	public PersonalBankAccountSettingDto() {}

	public PersonalBankAccountSettingDto(PersonalBankAccountSetting setting) {
		if(setting != null) {
			this.id = setting.getId();
			this.defaultNational = setting.getDefaultNational();
			this.defaultCode = setting.getDefaultCode();
		}

	}
}
