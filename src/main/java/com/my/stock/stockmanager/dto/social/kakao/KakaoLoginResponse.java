package com.my.stock.stockmanager.dto.social.kakao;

import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KakaoLoginResponse {
	private KakaoProfile profile;
	private String email;
	private Long memberId;
	private List<BankAccountDto> bankAccounts;
	private ExchangeRate exchangeRate;
}
