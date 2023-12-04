package com.my.stock.stockmanager.exception;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<BaseResponse> handleInternalServerError(Exception e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(ResponseCode.INTERNAL_ERROR, ResponseCode.INTERNAL_ERROR.getMessage()));
	}

	@ExceptionHandler(StockManagerException.class)
	public ResponseEntity<BaseResponse> handleStockManagerException(StockManagerException e) {
		HttpStatus status = switch (e.getCode().getErrorLevel()) {
			case WARN -> HttpStatus.OK;
			default -> HttpStatus.INTERNAL_SERVER_ERROR;
		};

		return ResponseEntity.status(status).body(new BaseResponse(ResponseCode.INTERNAL_ERROR, e.getMessage()));
	}
}
