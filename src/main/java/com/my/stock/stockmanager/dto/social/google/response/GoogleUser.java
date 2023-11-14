package com.my.stock.stockmanager.dto.social.google.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUser {
	private String id;
	private String name;
	private String given_name;
	private String family_name;
	private String picture;
	private String locale;
}
