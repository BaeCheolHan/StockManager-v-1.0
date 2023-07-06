package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.member.response.MemberResponse;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping
	public BaseResponse saveMember(@RequestBody Member member) {
		memberService.save(member);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping
	public MemberResponse find(Long id) {
		Member entity = memberService.findById(id);
		return MemberResponse.builder().code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).data(entity).build();
	}

}
