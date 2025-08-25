package com.my.stock.stockmanager.dto.social.google.rqeust;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleTokenRequest {
	private String grant_type = "authorization_code";
	private String client_id;
	private String client_secret;
	private String redirect_uri;
	private String code;

}
