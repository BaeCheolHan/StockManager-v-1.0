package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.api.yfin.YfinClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yfin")
@RequiredArgsConstructor
public class YfinProxyController {

    private final YfinClient yfinClient;

    @GetMapping("/indicators/ma")
    public Object ma(@RequestParam String ticker, @RequestParam int window) {
        return yfinClient.getMa(ticker, window);
    }

    @GetMapping("/indicators/rsi")
    public Object rsi(@RequestParam String ticker, @RequestParam(defaultValue = "14") int window) {
        return yfinClient.getRsi(ticker, window);
    }

    @GetMapping("/search/google")
    public Object searchGoogle(@RequestParam String q, @RequestParam(defaultValue = "10") int count, @RequestParam(required = false) String lang) {
        return yfinClient.searchGoogle(q, count, lang);
    }

    @GetMapping("/history")
    public Object history(@RequestParam String ticker,
                          @RequestParam String range,
                          @RequestParam String interval,
                          @RequestParam(defaultValue = "true") boolean autoAdjust,
                          @RequestParam(required = false) String exchange) {
        return yfinClient.getHistory(ticker, range, interval, autoAdjust, exchange);
    }

    @GetMapping("/earnings/dates")
    public Object earningsDates(@RequestParam String ticker, @RequestParam(required = false) String exchange) {
        return yfinClient.getEarningsDates(ticker, exchange);
    }

    @GetMapping("/corp-actions")
    public Object corpActions(@RequestParam String ticker, @RequestParam(required = false) String exchange) {
        return yfinClient.getCorpActions(ticker, exchange);
    }

    @GetMapping("/screener/filter")
    public Object screenerFilter(@RequestParam(required = false) String market,
                                 @RequestParam(required = false) Double minYield,
                                 @RequestParam(required = false) Double maxVolatility,
                                 @RequestParam(required = false) Long minVolume,
                                 @RequestParam(required = false) Long minMarketCap,
                                 @RequestParam(required = false) Long maxMarketCap,
                                 @RequestParam(required = false) String sortBy,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "30") int size) {
        return yfinClient.screenerFilter(market, minYield, maxVolatility, minVolume, minMarketCap, maxMarketCap, sortBy, page, size);
    }

    @GetMapping("/screener/sector/ranking")
    public Object screenerSectorRanking(@RequestParam(required = false) String market,
                                        @RequestParam(defaultValue = "10") int topN,
                                        @RequestParam(defaultValue = "change") String by) {
        return yfinClient.screenerSectorRanking(market, topN, by);
    }
}


