package com.my.stock.stockmanager.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Bank implements BankEnumMapperType{
	BOK("001", BankType.BANK, "한국은행"),
	KDB("002", BankType.BANK, "산업은행"),
	IBK("003", BankType.BANK, "기업은행"),
	KB("004", BankType.BANK, "국민은행"),
	SU_HYUP("007", BankType.BANK, "수협중앙회"),
	KOREAEXIM("008", BankType.BANK, "수출입은행"),
	NH("011", BankType.BANK, "농협은행"),
	NH_SUB("012", BankType.BANK, "지역농축협"),
	WOO_RI("020", BankType.BANK, "우리은행"),
	SC("023", BankType.BANK, "SC제일은행 / SC 은행"),
	CT("027", BankType.BANK, "한국씨티은행"),
	DGB("031", BankType.BANK, "대구은행"),
	BNK("032", BankType.BANK, "부산은행"),
	KJ("034", BankType.BANK, "광주은행"),
	JEJU("035", BankType.BANK, "제주은행"),
	JB("037", BankType.BANK, "전북은행"),
	KN("039", BankType.BANK, "경남은행"),
	WOORI_C("041", BankType.CARD, "우리카드"),
	KFCC("045", BankType.BANK, "새마을금고"),
	CU("049", BankType.BANK, "신협"),
	POST("071", BankType.BANK, "우체국"),
	KEB("081", BankType.BANK, "KEB하나은행"),
	SHIN_HAN("088", BankType.BANK, "신한은행"),
	K_BANK("089", BankType.BANK, "케이뱅크"),
	KAKAO_BANK("090", BankType.BANK, "카카오뱅크"),
	TOSS_BANK("092", BankType.BANK, "토스뱅크"),
	YUANTA("209", BankType.STOCK, "유안타증권"),
	KB_S("218", BankType.STOCK, "KB증권"),
	GBAM("221", BankType.STOCK, "골든브릿지투자증권"),
	HY("222", BankType.STOCK, "한양증권"),
	LEADING("223", BankType.STOCK, "리딩투자증권"),
	BNK_S("224", BankType.STOCK, "BNK투자증권"),
	IBK_S("225", BankType.STOCK, "IBK투자등권"),
	KTB_S("227", BankType.STOCK, "KTB투자증권"),
	MIRAE_S("238", BankType.STOCK, "미래에셋증권"),
	SAMSUNG_S("240", BankType.STOCK, "삼성증권"),
	KOR_INVEST_S("243", BankType.STOCK, "한국투자증권"),
	NH_S("247", BankType.STOCK, "NH투자증권"),
	IPROVEST("261", BankType.STOCK, "교보증권"),
	HI_IB("262", BankType.STOCK, "하이투자증권"),
	HMSEC("263", BankType.STOCK, "현대차증권"),
	KI_WOOM("264", BankType.STOCK, "키움증권"),
	EBESTSEC("265", BankType.STOCK, "이베스트투자증권"),
	SK_S("266", BankType.STOCK, "SK증권"),
	DAI_SIN("267", BankType.STOCK, "대신증권"),
	HANWHA_S("269", BankType.STOCK, "한화투자증권"),
	HANAW_S("270", BankType.STOCK, "하나금융투자"),
	SHINHAN_S("278", BankType.STOCK, "신한금융투자"),
	DB_FI_S("279", BankType.STOCK, "DB금융투자"),
	EUGENEFN_S("280", BankType.STOCK, "유진투자증권"),
	MERITZ_S("287", BankType.STOCK, "메리츠증권"),
	BOOKOOK_S("290", BankType.STOCK, "부국증권"),
	TOSS_S("271", BankType.STOCK, "토스증권"),
	KAKAO_PAY_S("288", BankType.STOCK, "카카오페이증권"),
	WOORIIB_S("295", BankType.STOCK, "우리종합금융");


	private final String code;
	private final BankType type;
	private final String bankName;

	Bank(String code, BankType type, String bankName) {
		this.code = code;
		this.type = type;
		this.bankName = bankName;
	}

	@JsonValue
	public String getCode() {
		return code;
	}

	public String getBankCode() {
		return name();
	}

	public BankType getType() {
		return type;
	}

	public String getBankName() {
		return bankName;
	}

}
