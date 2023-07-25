package com.my.stock.stockmanager.dto.personal.setting.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.rdb.entity.PersonalBankAccountSetting;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PersonalBankAccountSettingResponse extends BaseResponse {
	PersonalBankAccountSetting setting;

	@Builder
	public PersonalBankAccountSettingResponse(PersonalBankAccountSetting setting, ResponseCode code, String message) {
		super(code, message);
		this.setting = setting;
	}
}
