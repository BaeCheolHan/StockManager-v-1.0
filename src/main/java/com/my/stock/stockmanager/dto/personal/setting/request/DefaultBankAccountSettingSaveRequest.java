package com.my.stock.stockmanager.dto.personal.setting.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultBankAccountSettingSaveRequest {
	private String defaultNational;
	private String defaultMarket;
}
