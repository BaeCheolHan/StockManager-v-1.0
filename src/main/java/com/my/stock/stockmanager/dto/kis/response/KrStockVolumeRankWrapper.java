package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KrStockVolumeRankWrapper {
	private String rt_cd;
	private String msg_cd;
	private String msg1;
	private List<KrStockVolumeRankOutput> output;
}
