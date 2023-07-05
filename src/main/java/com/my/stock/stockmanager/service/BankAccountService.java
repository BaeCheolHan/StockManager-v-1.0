package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.bank.account.BankAccountDto;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.dto.request.BankAccountSaveRequest;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
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

	@Transactional
	public void save(BankAccountSaveRequest request) {
		Member memberEntity = memberRepository.findById(request.getMemberId()).orElseThrow(() -> new StockManagerException("존재하지 않는 사용자 식별키입니다.", ResponseCode.NOT_FOUND_ID));
		BankAccount account = new BankAccount();
		account.setBank(request.getBank());
		account.setMember(memberEntity);
		account.setAlias(request.getAlias());
		bankAccountRepository.save(account);
	}

	public BankAccount findById(Long id) {
		BankAccount account = bankAccountRepository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
		return null;

	}

	@Transactional
	public List<BankAccountDto> findBankAccountByMemberId(Long memberId) {
		Member memberEntity = memberRepository.findById(memberId).orElseThrow(() -> new StockManagerException("존재하지 않는 사용자 식별키입니다.", ResponseCode.NOT_FOUND_ID));
		return memberEntity.getBankAccount().stream().map(BankAccountDto::new).collect(Collectors.toList());
	}
}
