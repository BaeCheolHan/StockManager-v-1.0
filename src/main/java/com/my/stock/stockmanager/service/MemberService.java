package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.data.service.MemberDataService;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	private final MemberDataService memberDataService;
	public void save(Member member) {
		memberRepository.save(member);
	}

	@Transactional
	public Member findById(Long id) throws StockManagerException {
		return memberDataService.findById(id);
	}
}
