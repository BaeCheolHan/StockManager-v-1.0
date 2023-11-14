package com.my.stock.stockmanager.dto.social.google.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleToken {
	private String access_token;
	private String scope;
	private String token_type;
	private String id_token;
}
