package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.dto.personal.setting.request.DefaultBankAccountSettingSaveRequest;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.PersonalSettingRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalSettingService {
	private final BankAccountRepository bankAccountRepository;
	private final MemberRepository memberRepository;

	private final PersonalSettingRepository personalSettingRepository;

	private final StockRepository stockRepository;

	private final EntityManager entityManager;


	public void saveDefaultBankAccountSetting(Long memberId, DefaultBankAccountSettingSaveRequest defaultBankAccountSettingSaveRequest) {
	}
}
