package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.personal.setting.PersonalBankAccountSettingDto;
import com.my.stock.stockmanager.dto.personal.setting.response.PersonalBankAccountSettingResponse;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.service.PersonalSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-setting")
@RequiredArgsConstructor
public class PersonalSettingController {

	private final PersonalSettingService personalSettingService;

	@GetMapping("/{bankAccountId}")
	public PersonalBankAccountSettingResponse findByBankAccountId(@PathVariable Long bankAccountId) throws StockManagerException {
		return new PersonalBankAccountSettingResponse(personalSettingService.findByBankAccountId(bankAccountId), ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}


	@PutMapping("/{bankAccountId}")
	public BaseResponse savePersonalSetting(@PathVariable Long bankAccountId, @RequestBody PersonalBankAccountSettingDto personalBankAccountSettingSaveRequest) throws StockManagerException {
		personalSettingService.savePersonalBankAccountSetting(bankAccountId, personalBankAccountSettingSaveRequest);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

}
