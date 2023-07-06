package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.Stock;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;

	private final BankAccountRepository bankAccountRepository;

	public List<DashboardStock> getStocks(Long memberId, Long bankId) {
		return stockRepository.findAllDashboardStock(memberId, bankId);
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
