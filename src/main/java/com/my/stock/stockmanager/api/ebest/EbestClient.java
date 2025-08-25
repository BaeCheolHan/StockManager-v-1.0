package com.my.stock.stockmanager.api.ebest;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * TODO: 기존 EbestApi를 점진적으로 대체할 Feign 인터페이스 초안.
 * 실제 엔드포인트/헤더/요청/응답 DTO는 기존 구현을 분석하여 정의 필요.
 */
@FeignClient(name = "ebest", url = "")
public interface EbestClient {
}


