package com.my.stock.stockmanager.rdb.data.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountDataService {
	private final BankAccountRepository repository;

	public BankAccount findById(Long id) throws StockManagerException {
		return repository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}

	public BankAccount findByIdOrElseGetNew(Long id) {
		return repository.findById(id).orElseGet(BankAccount::new);
	}

	public BankAccount findByIdJoinFetch(Long id) throws StockManagerException {
		return repository.findByIdJoinFetch(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}
}
