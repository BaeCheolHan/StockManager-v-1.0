package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.dto.personal.setting.PersonalBankAccountSettingDto;
import com.my.stock.stockmanager.rdb.entity.PersonalBankAccountSetting;
import com.my.stock.stockmanager.rdb.repository.PersonalBankAccountSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalSettingService {

	private final PersonalBankAccountSettingRepository personalBankAccountSettingRepository;

	public PersonalBankAccountSetting findByBankAccountId(Long bankAccountId) {
		return personalBankAccountSettingRepository.findByBankAccountId(bankAccountId).orElse(null);
	}

	public void savePersonalBankAccountSetting(Long bankAccountId, PersonalBankAccountSettingDto personalBankAccountSettingSaveRequest) {
		Optional<PersonalBankAccountSetting> opt = personalBankAccountSettingRepository.findByBankAccountId(bankAccountId);

		opt.ifPresentOrElse(entity -> {
					entity.setDefaultNational(personalBankAccountSettingSaveRequest.getDefaultNational());
					entity.setDefaultCode(personalBankAccountSettingSaveRequest.getDefaultCode());
					personalBankAccountSettingRepository.save(entity);
				},
				() -> {
					PersonalBankAccountSetting entity = new PersonalBankAccountSetting();
					entity.setBankAccountId(bankAccountId);
					entity.setDefaultNational(personalBankAccountSettingSaveRequest.getDefaultNational());
					entity.setDefaultCode(personalBankAccountSettingSaveRequest.getDefaultCode());
					personalBankAccountSettingRepository.save(entity);
				});

	}
}
