package com.my.stock.stockmanager.dto.ebest.token.request;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EbestTokenRequest {
	private final String grant_type = "client_credentials";
	@Setter
	private String appkey;

	@Setter
	private String appsecretkey;

	private final String scope = "oob";

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("grant_type", this.grant_type);
		map.put("appkey", this.appkey);
		map.put("appsecretkey", this.appsecretkey);
		map.put("scope", this.scope);
		return map;
	}
}
