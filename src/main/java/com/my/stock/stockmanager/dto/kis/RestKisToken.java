package com.my.stock.stockmanager.dto.kis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestKisToken {
	private String access_token;
	private String access_token_token_expired;
	private String token_type;
	private int expires_in;
}
