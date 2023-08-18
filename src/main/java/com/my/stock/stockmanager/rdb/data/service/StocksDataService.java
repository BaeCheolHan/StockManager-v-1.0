package com.my.stock.stockmanager.rdb.data.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.rdb.repository.StocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StocksDataService {
	private final StocksRepository repository;


	public Stocks findBySymbol(String symbol) throws StockManagerException {
		return repository.findBySymbol(symbol).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}

}
