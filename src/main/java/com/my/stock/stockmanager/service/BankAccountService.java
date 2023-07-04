package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.dto.request.BankAccountSaveRequest;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BankAccountService {
	private final BankAccountRepository bankAccountRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void save(BankAccountSaveRequest request) {
		Member memberEntity = memberRepository.findById(request.getMemberId()).orElseThrow(() -> new StockManagerException("존재하지 않는 사용자 식별키입니다.", ResponseCode.NOT_FOUND_ID));
		request.getBankAccount().setMember(memberEntity);
		bankAccountRepository.save(request.getBankAccount());
	}

	public BankAccount findById(Long id) {
		return bankAccountRepository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}
}
