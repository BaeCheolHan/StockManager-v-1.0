package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.personal.setting.PersonalBankAccountSettingDto;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.data.service.BankAccountDataService;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.PersonalBankAccountSetting;
import com.my.stock.stockmanager.rdb.entity.PersonalSetting;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.PersonalBankAccountSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalSettingService {

	private final PersonalBankAccountSettingRepository personalBankAccountSettingRepository;

	private final BankAccountRepository bankAccountRepository;

	private final BankAccountDataService bankAccountDataService;

	@Transactional
	public PersonalBankAccountSettingDto findByBankAccountId(Long bankAccountId) throws StockManagerException {
		return new PersonalBankAccountSettingDto(bankAccountDataService.findById(bankAccountId).getPersonalBankAccountSetting());
	}

	@Transactional
	public void savePersonalBankAccountSetting(Long bankAccountId, PersonalBankAccountSettingDto personalBankAccountSettingSaveRequest) throws StockManagerException {
		BankAccount bankAccount = bankAccountDataService.findById(bankAccountId);

		PersonalBankAccountSetting personalBankAccountSetting = bankAccount.getPersonalBankAccountSetting();

		if(personalBankAccountSetting != null) {
			personalBankAccountSetting.setDefaultNational(personalBankAccountSettingSaveRequest.getDefaultNational());
			personalBankAccountSetting.setDefaultCode(personalBankAccountSettingSaveRequest.getDefaultCode());
			personalBankAccountSettingRepository.save(personalBankAccountSetting);
		} else {
			personalBankAccountSetting = new PersonalBankAccountSetting();
			personalBankAccountSetting.setBankAccount(bankAccount);
			personalBankAccountSetting.setDefaultNational(personalBankAccountSettingSaveRequest.getDefaultNational());
			personalBankAccountSetting.setDefaultCode(personalBankAccountSettingSaveRequest.getDefaultCode());
			personalBankAccountSettingRepository.save(personalBankAccountSetting);
			bankAccount.setPersonalBankAccountSetting(personalBankAccountSetting);
			bankAccountRepository.save(bankAccount);
		}

	}
}
