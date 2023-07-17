package com.my.stock.stockmanager.dto.dividend.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DividendRequest {

	private Long memberId;
	private String symbol;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private BigDecimal dividend;


}
