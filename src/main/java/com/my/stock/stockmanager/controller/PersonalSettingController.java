package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.dto.personal.setting.request.DefaultBankAccountSettingSaveRequest;
import com.my.stock.stockmanager.service.PersonalSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-setting")
@RequiredArgsConstructor
public class PersonalSettingController {

	private final PersonalSettingService personalSettingService;

	@PostMapping("/{memberId}")
	public void savePersonalSetting(@PathVariable Long memberId, @RequestBody DefaultBankAccountSettingSaveRequest defaultBankAccountSettingSaveRequest) {
		personalSettingService.saveDefaultBankAccountSetting(memberId, defaultBankAccountSettingSaveRequest);
	}

}
