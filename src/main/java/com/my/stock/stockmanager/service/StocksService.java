package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.rdb.entity.StockSearchStat;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.rdb.repository.StockSearchStatRepository;
import com.my.stock.stockmanager.rdb.repository.StocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StocksService {

	private final StocksRepository repository;
	private final StockSearchStatRepository statRepository;

	public List<String> getStocksCodeListByNational(String national) {
		return repository.findCodeByNationalGroupByCode(national);
	}

	public List<Stocks> getStocksListByMarketCode(String code) {
		return repository.findAllByCode(code);
	}

	public List<Stocks> getStockBySymbol(String symbol) {
		return repository.findAllBySymbolLike(symbol);
	}

	public List<Stocks> getAllStocks() {
		return repository.findAll();
	}

	public List<Stocks> search(String q, int limit) {
		String qprefix = q + "%";
		return repository.searchByKeyword(q, qprefix, limit);
	}

	public List<Stocks> trending(int limit) {
		return repository.findTrending(Math.max(1, Math.min(limit, 50)));
	}

	@Transactional
	public void increaseSearchHit(String symbol) {
		StockSearchStat stat = statRepository.findById(symbol)
				.orElse(StockSearchStat.builder().symbol(symbol).hitCount(0).lastHitAt(LocalDateTime.now()).build());
		stat.increase();
		statRepository.save(stat);
	}
}
