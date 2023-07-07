package com.my.stock.stockmanager.dto.bank.account.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BankAccountResponse extends BaseResponse {
	private List<BankAccountDto> accounts;

	@Builder
	public BankAccountResponse(ResponseCode code, String message, List<BankAccountDto> accounts) {
		super(code, message);
		this.accounts = accounts;
	}
}
