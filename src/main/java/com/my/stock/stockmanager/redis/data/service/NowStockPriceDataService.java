package com.my.stock.stockmanager.redis.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.dto.kis.request.KrStockPriceRequest;
import com.my.stock.stockmanager.dto.kis.request.OverSeaStockPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrNowStockPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaNowStockPriceWrapper;
import com.my.stock.stockmanager.rdb.data.service.StocksDataService;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.redis.repository.KrNowStockPriceRepository;
import com.my.stock.stockmanager.redis.repository.OverSeaNowStockPriceRepository;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;

@Service
@RequiredArgsConstructor
public class NowStockPriceDataService {

    private final OverSeaNowStockPriceRepository overSeaRepo;
    private final KrNowStockPriceRepository krRepo;
    private final DividendInfoDataService dividendInfoDataService;
    private final StocksDataService stocksDataService;
    private final KisApi kisApi;
    private final KisApiUtils kisApiUtils;

    @Transactional(readOnly = true)
    public KrNowStockPrice findKrById(String symbol) {
        KrNowStockPrice price = krRepo.findById(symbol).orElseGet(() -> {
            HttpHeaders headers;
            try {
                headers = kisApiUtils.getDefaultApiHeader("FHKST01010100");
            } catch (MalformedURLException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            KrStockPriceRequest request = KrStockPriceRequest.builder()
                    .fid_cond_mrkt_div_code("J")
                    .fid_input_iscd(symbol)
                    .build();

            KrNowStockPriceWrapper response = kisApi.getKorStockPrice(headers, request);
            response.getOutput().setSymbol(symbol);

            DividendInfo dividendInfo = dividendInfoDataService.findByIdOrElseNew(symbol);
            response.getOutput().setDividendInfo(dividendInfo);
            krRepo.save(response.getOutput());
            return response.getOutput();
        });

        if (price.getDividendInfo() == null) {
            DividendInfo dividendInfo = dividendInfoDataService.findByIdOrElseNew(symbol);
            price.setDividendInfo(dividendInfo);
            krRepo.save(price);
        }
        return price;
    }

    @Transactional(readOnly = true)
    public OverSeaNowStockPrice findOverSeaById(String symbol) {
        OverSeaNowStockPrice entity = overSeaRepo.findById(symbol).orElseGet(() -> {
            try {
                Stocks stocks = stocksDataService.findBySymbol(symbol);
                HttpHeaders headers = kisApiUtils.getDefaultApiHeader("HHDFS76200200");
                headers.add("custtype", "P");

                OverSeaNowStockPriceWrapper response = kisApi.getOverSeaStockPrice(headers, OverSeaStockPriceRequest.builder()
                        .AUTH("")
                        .EXCD(stocks.getCode())
                        .SYMB(stocks.getSymbol())
                        .build());
                response.getOutput().setSymbol(symbol);

                DividendInfo dividendInfo = dividendInfoDataService.findByIdOrElseNew(symbol);
                response.getOutput().setDividendInfo(dividendInfo);
                overSeaRepo.save(response.getOutput());
                return response.getOutput();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        if (entity.getDividendInfo() == null) {
            DividendInfo dividendInfo = dividendInfoDataService.findByIdOrElseNew(symbol);
            entity.setDividendInfo(dividendInfo);
            overSeaRepo.save(entity);
        }
        return entity;
    }
}


