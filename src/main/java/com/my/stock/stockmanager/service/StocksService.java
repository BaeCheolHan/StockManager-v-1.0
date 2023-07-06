package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.rdb.repository.StocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StocksService {

	private final StocksRepository repository;

	public List<Stocks> getStocksListEitherKrAndOverSea(String national) {
		if(national.equals("KR")) {
			return repository.findAllByNational(national);
		} else {
			return repository.findAllByNationalNot(national);
		}

	}
}
