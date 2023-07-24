package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.Bank;
import com.my.stock.stockmanager.constants.BankType;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.bank.account.BankEnumMapperValue;
import com.my.stock.stockmanager.dto.bank.account.request.BankAccountSaveRequest;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountResponse;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
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
	public BaseResponse save(@RequestBody BankAccountSaveRequest request) throws StockManagerException {
		bankAccountService.save(request);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/bank/member/{memberId}")
	public BankAccountResponse findBankAccountByMemberId(@PathVariable Long memberId) throws StockManagerException {
		List<BankAccountDto> accounts = bankAccountService.findBankAccountByMemberId(memberId);
		return new BankAccountResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), accounts);
	}

	@GetMapping("/banks")
	public List<BankEnumMapperValue> getBankList() {
		return Arrays.stream(Bank.values())
				.filter(item -> item.getType().equals(BankType.STOCK))
				.map(BankEnumMapperValue::new)
				.collect(Collectors.toList());
	}

	@PutMapping("/default-bank/{memberId}/{id}")
	public BaseResponse saveDefaultBank(@PathVariable Long memberId, @PathVariable Long id) throws StockManagerException {
		bankAccountService.saveDefaultBank(memberId, id);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@DeleteMapping("/bank/{id}")
	public BankAccountResponse deleteBank(@PathVariable Long id) throws StockManagerException {
		return new BankAccountResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), bankAccountService.deleteBank(id));
	}



}
