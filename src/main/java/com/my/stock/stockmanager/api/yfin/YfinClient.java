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

    // Proxy passthroughs used by client features (shape is controlled by yfin-java-lite)
    @GetMapping("/indicators/ma")
    Object getMa(@RequestParam("ticker") String ticker, @RequestParam("window") int window);

    @GetMapping("/indicators/rsi")
    Object getRsi(@RequestParam("ticker") String ticker, @RequestParam(value = "window", defaultValue = "14") int window);

    @GetMapping("/search/google")
    Object searchGoogle(@RequestParam("q") String q, @RequestParam(value = "count", defaultValue = "10") int count, @RequestParam(value = "lang", required = false) String lang);

    @GetMapping("/history")
    Object getHistory(@RequestParam("ticker") String ticker,
                      @RequestParam("range") String range,
                      @RequestParam("interval") String interval,
                      @RequestParam(value = "autoAdjust", defaultValue = "true") boolean autoAdjust,
                      @RequestParam(value = "exchange", required = false) String exchange);

    @GetMapping("/earnings/dates")
    Object getEarningsDates(@RequestParam("ticker") String ticker,
                            @RequestParam(value = "exchange", required = false) String exchange);

    @GetMapping("/corp-actions")
    Object getCorpActions(@RequestParam("ticker") String ticker,
                          @RequestParam(value = "exchange", required = false) String exchange);

    @GetMapping("/screener/filter")
    Object screenerFilter(@RequestParam(value = "market", required = false) String market,
                          @RequestParam(value = "minYield", required = false) Double minYield,
                          @RequestParam(value = "maxVolatility", required = false) Double maxVolatility,
                          @RequestParam(value = "minVolume", required = false) Long minVolume,
                          @RequestParam(value = "minMarketCap", required = false) Long minMarketCap,
                          @RequestParam(value = "maxMarketCap", required = false) Long maxMarketCap,
                          @RequestParam(value = "sortBy", required = false) String sortBy,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "30") int size);

    @GetMapping("/screener/sector/ranking")
    Object screenerSectorRanking(@RequestParam(value = "market", required = false) String market,
                                 @RequestParam(value = "topN", defaultValue = "10") int topN,
                                 @RequestParam(value = "by", defaultValue = "change") String by);
}


