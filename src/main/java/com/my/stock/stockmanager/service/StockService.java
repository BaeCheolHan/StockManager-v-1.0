package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;

	MemberRepository memberRepository;

	BankAccountRepository bankAccountRepository;
	public List<DashboardStock> getStocks(Long memberId, Long bankId) {
		return stockRepository.findAllDashboardStock(memberId, bankId);
	}
}
