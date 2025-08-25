package com.my.stock.stockmanager.redis.data.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.stock.stockmanager.dto.dividend.StockDividendHistory;
import com.my.stock.stockmanager.api.yfin.YfinClient;
import com.my.stock.stockmanager.api.yfin.dto.DividendsDto;
import com.my.stock.stockmanager.api.yfin.dto.QuoteDto;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import com.my.stock.stockmanager.redis.repository.DividendInfoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DividendInfoDataService {

	private final DividendInfoRepository dividendInfoRepository;
	private final YfinClient yfinClient;

	public DividendInfo findByIdOrElseNew(String symbol) {
		return dividendInfoRepository.findById(symbol).orElseGet(() -> fetchFromYfinAndSave(symbol));
	}

	private DividendInfo fetchFromYfinAndSave(String symbol) {
		try {
			DividendsDto divRes = yfinClient.getDividends(symbol, "5y");

			List<StockDividendHistory> histories = new ArrayList<>();
			BigDecimal ttm = BigDecimal.ZERO;
			LocalDate cutoff = LocalDate.now().minusDays(365);

			if (divRes != null && divRes.getRows() != null) {
				for (DividendsDto.Row r : divRes.getRows()) {
					if (r == null || r.getAmount() == null || r.getDate() == null) continue;
					LocalDate d = r.getDate().atZone(ZoneOffset.UTC).toLocalDate();
					histories.add(StockDividendHistory.builder()
							.symbol(symbol)
							.date(d.toString())
							.dividend(BigDecimal.valueOf(r.getAmount()))
							.build());
					if (!d.isBefore(cutoff)) {
						ttm = ttm.add(BigDecimal.valueOf(r.getAmount()));
					}
				}
			}

			BigDecimal forwardYieldPct = BigDecimal.ZERO;
			try {
				QuoteDto q = yfinClient.getQuote(symbol);
				if (q != null && q.getForwardDividendYieldPct() != null) {
					forwardYieldPct = BigDecimal.valueOf(q.getForwardDividendYieldPct()).setScale(2, RoundingMode.HALF_UP);
				}
			} catch (Exception ignore) {}

			DividendInfo info = new DividendInfo();
			info.setSymbol(symbol);
			info.setDividendHistories(histories);
			info.setAnnualDividend(ttm.setScale(4, RoundingMode.HALF_UP));
			info.setDividendRate(forwardYieldPct);

			return dividendInfoRepository.save(info);
		} catch (Exception e) {
			return new DividendInfo();
		}
	}

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class YfinDividendsResponse {}
}
