package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.request.DividendRequest;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.Dividend;
import com.my.stock.stockmanager.rdb.repository.DividendRepository;
import com.my.stock.stockmanager.rdb.repository.StocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DividendService {
	private final DividendRepository repository;

	private final StocksRepository stocksRepository;

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
}
