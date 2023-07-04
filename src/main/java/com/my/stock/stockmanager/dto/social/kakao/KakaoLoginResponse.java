package com.my.stock.stockmanager.dto.social.kakao;

import com.my.stock.stockmanager.dto.bank.account.BankAccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoLoginResponse {
	private KakaoProfile profile;
	private String email;
	private Long memberId;
	private List<BankAccountDto> bankAccounts;
}
