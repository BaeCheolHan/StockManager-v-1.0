package com.my.stock.stockmanager.rdb.data.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDataService {
	private final MemberRepository repository;

	public Member findById(Long id) throws StockManagerException {
		return repository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}

	public Member findByIdOrElseGetNew(Long id) {
		return repository.findById(id).orElseGet(Member::new);
	}
}
