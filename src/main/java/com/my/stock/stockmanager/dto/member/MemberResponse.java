package com.my.stock.stockmanager.dto.member;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.rdb.entity.Member;
import lombok.*;

@Getter
public class MemberResponse extends BaseResponse {
    private Member data;

    @Builder
    public MemberResponse(Member data, ResponseCode code, String message) {
        super(code, message);
        this.data = data;
    }
}
