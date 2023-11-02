package com.my.stock.stockmanager.base.response;

import com.my.stock.stockmanager.constants.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseResponse {
    private ResponseCode code;
    private String message;
}
