package com.my.stock.stockmanager.dto.social.kakao.response;

import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoginResponse {
	private String nickName;
	private String profileImgUrl;
	private String email;
	private String memberId;
	private List<BankAccountDto> bankAccounts;
	private ExchangeRate exchangeRate;
	private Long defaultBankAccountId;
}
