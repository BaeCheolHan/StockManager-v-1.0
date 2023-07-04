package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.Bank;
import com.my.stock.stockmanager.constants.BankType;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.bank.account.BankEnumMapperValue;
import com.my.stock.stockmanager.rdb.dto.request.BankAccountSaveRequest;
import com.my.stock.stockmanager.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class BankAccountController {
	private final BankAccountService bankAccountService;

	@PostMapping
	public BaseResponse save(@RequestBody BankAccountSaveRequest request) {
		bankAccountService.save(request);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping
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
