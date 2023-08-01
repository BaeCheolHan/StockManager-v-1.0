package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.dto.bank.account.request.BankAccountSaveRequest;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.data.service.BankAccountDataService;
import com.my.stock.stockmanager.rdb.data.service.MemberDataService;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.entity.PersonalSetting;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.PersonalSettingRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountService {
	private final BankAccountRepository bankAccountRepository;

	private final PersonalSettingRepository personalSettingRepository;

	private final StockRepository stockRepository;

	private final EntityManager entityManager;

	private final MemberDataService memberDataService;

	private final BankAccountDataService bankAccountDataService;

	@Transactional
	public void save(BankAccountSaveRequest request) throws StockManagerException {
		Member memberEntity = memberDataService.findById(request.getMemberId());

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
		Member memberEntity = memberDataService.findById(memberId);
		return memberEntity.getBankAccount().stream().map(BankAccountDto::new).collect(Collectors.toList());
	}

	@Transactional
	public void saveDefaultBank(Long memberId, Long id) throws StockManagerException {
		Member memberEntity = memberDataService.findById(memberId);
		PersonalSetting setting = memberEntity.getPersonalSetting();

		if (setting == null) {
			setting = new PersonalSetting();
			setting.setMember(memberEntity);
		}
		setting.setDefaultBankAccountId(id);
		personalSettingRepository.save(setting);
	}

	@Transactional
	public List<BankAccountDto> deleteBank(Long id) throws StockManagerException {
		BankAccount account = bankAccountDataService.findById(id);
		personalSettingRepository.findByDefaultBankAccountId(id).ifPresent(setting -> {
			setting.setDefaultBankAccountId(null);
			personalSettingRepository.save(setting);
		});

		stockRepository.deleteAllByBankAccountId(id);
		bankAccountRepository.deleteById(id);
		entityManager.flush();

		Member member = memberDataService.findById(account.getMember().getId());
		return member.getBankAccount().stream().map(BankAccountDto::new).collect(Collectors.toList());

	}
}
