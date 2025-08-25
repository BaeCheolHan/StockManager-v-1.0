package com.my.stock.stockmanager.api.yfin;

import com.my.stock.stockmanager.api.yfin.dto.DividendsDto;
import com.my.stock.stockmanager.api.yfin.dto.QuoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yfin", url = "http://127.0.0.1:8080")
public interface YfinClient {

    @GetMapping("/dividends")
    DividendsDto getDividends(@RequestParam("ticker") String ticker, @RequestParam(value = "range", required = false) String range);

    @GetMapping("/quote")
    QuoteDto getQuote(@RequestParam("ticker") String ticker);
}


