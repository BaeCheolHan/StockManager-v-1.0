package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.base.response.bank.accunt.BankAccountResponse;
import com.my.stock.stockmanager.constants.Bank;
import com.my.stock.stockmanager.constants.BankType;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.dto.bank.account.BankEnumMapperValue;
import com.my.stock.stockmanager.dto.bank.account.request.BankAccountSaveRequest;
import com.my.stock.stockmanager.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BankAccountController {
	private final BankAccountService bankAccountService;

	@PostMapping("/account")
	public BaseResponse save(@RequestBody BankAccountSaveRequest request) {
		bankAccountService.save(request);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/bank/member/{memberId}")
	public BankAccountResponse findBankAccountByMemberId(@PathVariable Long memberId) {
		List<BankAccountDto> accounts = bankAccountService.findBankAccountByMemberId(memberId);
		return new BankAccountResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), accounts);
	}

	@GetMapping("/bank")
	public void find(Long id) {
		bankAccountService.findById(id);
	}

	@GetMapping("/banks")
	public List<BankEnumMapperValue> getBankList() {
		return Arrays.stream(Bank.values())
				.filter(item -> item.getType().equals(BankType.STOCK))
				.map(BankEnumMapperValue::new)
				.collect(Collectors.toList());
	}


}
