package com.my.stock.stockmanager.exception;

import com.my.stock.stockmanager.constants.ResponseCode;
import lombok.Getter;

@Getter
public class StockManagerException extends Exception {

	private final ResponseCode code;

	public StockManagerException(String message, ResponseCode code) {
		super(message);
		this.code = code;
	}

	public StockManagerException(ResponseCode code) {
		super(code.getMessage());
		this.code = code;
	}
}
