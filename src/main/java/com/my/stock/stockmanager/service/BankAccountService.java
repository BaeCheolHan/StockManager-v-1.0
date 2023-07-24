package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.bank.account.request.BankAccountSaveRequest;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.entity.PersonalSetting;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.PersonalSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountService {
	private final BankAccountRepository bankAccountRepository;
	private final MemberRepository memberRepository;

	private final PersonalSettingRepository personalSettingRepository;

	@Transactional
	public void save(BankAccountSaveRequest request) throws StockManagerException {
		Member memberEntity = memberRepository.findById(request.getMemberId()).orElseThrow(() -> new StockManagerException("존재하지 않는 사용자 식별키입니다.", ResponseCode.NOT_FOUND_ID));

		PersonalSetting setting = null;
		if (memberEntity.getBankAccount() == null || memberEntity.getBankAccount().isEmpty()) {
			setting = new PersonalSetting();
			setting.setMember(memberEntity);
		}

		BankAccount account = new BankAccount();
		account.setBank(request.getBank());
		account.setMember(memberEntity);
		account.setAlias(request.getAlias());
		account = bankAccountRepository.save(account);

		if (setting != null) {
			setting.setDefaultBankAccountId(account.getId());
			personalSettingRepository.save(setting);
		}
	}

	@Transactional
	public List<BankAccountDto> findBankAccountByMemberId(Long memberId) throws StockManagerException {
		Member memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new StockManagerException("존재하지 않는 사용자 식별키입니다.", ResponseCode.NOT_FOUND_ID));
		return memberEntity.getBankAccount().stream().map(BankAccountDto::new).collect(Collectors.toList());
	}

	@Transactional
	public void saveDefaultBank(Long memberId, Long id) throws StockManagerException {
		Member memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new StockManagerException("존재하지 않는 사용자 식별키입니다.", ResponseCode.NOT_FOUND_ID));
		PersonalSetting setting = memberEntity.getPersonalSetting();

		if (setting == null) {
			setting = new PersonalSetting();
			setting.setMember(memberEntity);
			setting.setDefaultBankAccountId(id);
			personalSettingRepository.save(setting);
		} else {
			setting.setDefaultBankAccountId(id);
			personalSettingRepository.save(setting);
		}
	}
}
