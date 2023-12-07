package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.DividendChart;
import com.my.stock.stockmanager.dto.dividend.DividendInfoDto;
import com.my.stock.stockmanager.dto.dividend.DividendInfoByItem;
import com.my.stock.stockmanager.dto.dividend.DividendSumByMonth;
import com.my.stock.stockmanager.dto.dividend.request.DividendRequest;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.Dividend;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.repository.DividendRepository;
import com.my.stock.stockmanager.rdb.repository.StocksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DividendService {
	private final DividendRepository repository;

	private final StocksRepository stocksRepository;

	private final ExchangeRateService exchangeRateService;

	public void saveDividend(DividendRequest request) throws StockManagerException {

		stocksRepository.findBySymbol(request.getSymbol()).orElseThrow(() -> new StockManagerException("존재하지 않는 티커 입니다.", ResponseCode.NOT_FOUND_ID));

		Dividend entity = Dividend.builder()
				.memberId(request.getMemberId())
				.symbol(request.getSymbol())
				.year(request.getDate().getYear())
				.month(request.getDate().getMonthValue())
				.day(request.getDate().getDayOfMonth())
				.dividend(request.getDividend())
				.build();
		repository.save(entity);
	}

	public List<DividendChart> getDividendChart(String memberId) {
		List<Integer> years = repository.findYearByMemberIdGroupByYear(memberId);
		List<DividendSumByMonth> yearAndMonthlyDividend = repository.findDividendChartByMemberId(memberId);

		List<DividendChart> chartData = new ArrayList<>();
		List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		LocalDate toDay = LocalDate.now();
		for (Integer year : years) {
			DividendChart dataRow = new DividendChart();
			dataRow.setName(year.toString());

			List<DividendSumByMonth> annualDividendData = yearAndMonthlyDividend.stream().filter(it -> it.getYear() == year).toList();
			List<BigDecimal> chartSeries = new ArrayList<>();
			for (Integer month : months) {
				Optional<DividendSumByMonth> data = annualDividendData.stream().filter(it -> it.getMonth() == month).findFirst();
				chartSeries.add(data.isPresent() ? data.get().getDividend() : BigDecimal.ZERO);
			}

			dataRow.setData(chartSeries);
			dataRow.setTotal(chartSeries.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO).setScale(0, RoundingMode.FLOOR));
			dataRow.setAvg(dataRow.getTotal()
					.divide(String.valueOf(toDay.getYear()).equals(dataRow.getName()) ? BigDecimal.valueOf(toDay.getMonthValue()) : BigDecimal.valueOf(12), 2, RoundingMode.FLOOR)
			);
			chartData.add(dataRow);
		}

		for (int i = 1; i < chartData.size(); i++) {
			BigDecimal prev = chartData.get(i - 1).getAvg();
			BigDecimal after = chartData.get(i).getAvg();
			BigDecimal rate = after.divide(prev, 2, RoundingMode.FLOOR).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100));
			chartData.get(i).setChangeRate(rate);
		}

		return chartData;
	}

	@Transactional
	public List<DividendInfoDto> getDividends(String memberId) {
		Sort sort = Sort.by(Sort.Order.desc("year"), Sort.Order.desc("month"), Sort.Order.desc("day"));
		return repository.findAllByMemberIdOrderByYearMonthDayAsc(memberId, sort);
	}

	public List<DividendInfoByItem> getAllDividendsByItem(String memberId) throws IOException {
		ExchangeRate exchangeRate = exchangeRateService.getExchangeRate();

		return repository.findDividendInfoByMemberIdGroupBySymbol(memberId, exchangeRate.getBasePrice())
				.stream().sorted(Comparator.comparing(DividendInfoByItem::getTotalKrDividend).reversed()).collect(Collectors.toList());
	}

	public List<Dividend> getDividendsByItem(String memberId, String symbol) {
		Sort sort = Sort.by(Sort.Order.asc("year"), Sort.Order.asc("month"), Sort.Order.asc("day"));
		return repository.findAllDividendsByMemberIdAndSymbol(memberId, symbol, sort);
	}

	public void removeDividend(Long id) {
		repository.deleteById(id);
	}
}
