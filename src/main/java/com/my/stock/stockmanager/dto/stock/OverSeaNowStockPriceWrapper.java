package com.my.stock.stockmanager.dto.stock;

import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverSeaNowStockPriceWrapper {
    private OverSeaNowStockPrice output;
    private String rt_cd;
    private String msg_cd;
    private String msg1;
}
