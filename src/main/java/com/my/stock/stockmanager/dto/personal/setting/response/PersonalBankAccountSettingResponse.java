package com.my.stock.stockmanager.dto.personal.setting.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.personal.setting.PersonalBankAccountSettingDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PersonalBankAccountSettingResponse extends BaseResponse {
	private final PersonalBankAccountSettingDto setting;

	@Builder
	public PersonalBankAccountSettingResponse(PersonalBankAccountSettingDto setting, ResponseCode code, String message) {
		super(code, message);
		this.setting = setting;
	}
}
