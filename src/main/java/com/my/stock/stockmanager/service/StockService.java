package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.entity.Stock;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.redis.repository.KrNowStockPriceRepository;
import com.my.stock.stockmanager.redis.repository.OverSeaNowStockPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;

	private final ExchangeRateRepository exchangeRateRepository;

	private final BankAccountRepository bankAccountRepository;

	private final KrNowStockPriceRepository krNowStockPriceRepository;

	private final OverSeaNowStockPriceRepository overSeaNowStockPriceRepository;

	public List<DashboardStock> getStocks(Long memberId, Long bankId) {
		List<DashboardStock> stocks = stockRepository.findAllDashboardStock(memberId, bankId);
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();

		final double totalInvestmentAmount = stocks.stream()
				.mapToDouble(it -> {
					if (!it.getNational().equals("KR")) {
						return it.getAvgPrice() * exchangeRateList.get(exchangeRateList.size() - 1).getBasePrice() * it.getQuantity();
					} else {
						return it.getAvgPrice() * it.getQuantity();
					}
				}).sum();

		stocks.forEach(stock -> {
			if (!stock.getNational().equals("KR")) {
				stock.setPriceImportance((stock.getAvgPrice() * exchangeRateList.get(exchangeRateList.size() - 1).getBasePrice() * stock.getQuantity()) / totalInvestmentAmount * 100.0);
				Optional<OverSeaNowStockPrice> entity = overSeaNowStockPriceRepository.findById("D".concat(stock.getCode()).concat(stock.getSymbol()));
				entity.ifPresent(it ->{
					stock.setNowPrice(it.getLast());
					BigDecimal nowPrice1 = BigDecimal.valueOf(it.getLast());
					BigDecimal avgPrice = BigDecimal.valueOf(stock.getAvgPrice());

					stock.setRateOfReturnPer(nowPrice1.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100)).toString());
				});
			} else {
				stock.setPriceImportance((stock.getAvgPrice() * stock.getQuantity()) / totalInvestmentAmount * 100.0);
				Optional<KrNowStockPrice> entity = krNowStockPriceRepository.findById(stock.getSymbol());
				entity.ifPresent(it -> {
					stock.setNowPrice(it.getStck_prpr());
					BigDecimal nowPrice1 = BigDecimal.valueOf(it.getStck_prpr());
					BigDecimal avgPrice = BigDecimal.valueOf(stock.getAvgPrice());
					stock.setRateOfReturnPer(nowPrice1.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100)).toString());
				});
			}
		});
		return stocks.stream().sorted(Comparator.comparing(DashboardStock::getPriceImportance).reversed()).collect(Collectors.toList());
	}

	@Transactional
	public void saveStock(StockSaveRequest request) {
		BankAccount account = bankAccountRepository.findById(request.getBankId())
				.orElseThrow(() -> new StockManagerException("존재하지 않는 은행 식별키입니다.", ResponseCode.NOT_FOUND_ID));

		Stock stock = new Stock();
		stock.setBankAccount(account);
		stock.setPrice(request.getPrice());
		stock.setSymbol(request.getSymbol());
		stock.setQuantity(request.getQuantity());
		stockRepository.save(stock);
	}
}
