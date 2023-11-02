package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//	조회 기간별 시세
public class KrDailyIndexChartPriceOutput2 {
	private String acml_tr_pbmn;
	private String acml_vol;
	private String bstp_nmix_lwpr;
	private String bstp_nmix_hgpr;
	private String bstp_nmix_oprc;
	private String bstp_nmix_prpr;
	private String stck_bsop_date;
	private String mod_yn;
}
