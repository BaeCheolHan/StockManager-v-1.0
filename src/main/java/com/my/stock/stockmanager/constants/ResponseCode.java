package com.my.stock.stockmanager.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {
	INTERNAL_ERROR("서버 내부 오류 입니다.", ErrorLevel.CRITICAL),
	INVALID_ARGUMENT("입력값이 잘못 되었습니다.", ErrorLevel.WARN),
	NOT_FOUND_ID("존재하지 않는 식별키입니다.", ErrorLevel.WARN),
	SUCCESS("SUCCESS", ErrorLevel.OK);

	private final String message;
	private final ErrorLevel errorLevel;
}
