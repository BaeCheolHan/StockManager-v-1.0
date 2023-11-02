package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrDailyIndexChartPriceOutput1 {
	private String futs_prdy_lwpr;
	private String futs_prdy_hgpr;
	private String futs_prdy_oprc;
	private String bstp_nmix_lwpr;
	private String bstp_nmix_hgpr;
	private String bstp_nmix_oprc;
	private String prdy_vol;
	private String bstp_cls_code;
	private String bstp_nmix_prpr;
	private String hts_kor_isnm;
	private String acml_tr_pbmn;
	private String acml_vol;
	private String prdy_nmix;
	private String bstp_nmix_prdy_ctrt;
	private String prdy_vrss_sign;
	private String bstp_nmix_prdy_vrss;
}
