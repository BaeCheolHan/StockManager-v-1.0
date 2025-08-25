package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.service.StocksService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@WebMvcTest(controllers = StocksController.class, excludeAutoConfiguration = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@ImportAutoConfiguration
class StocksControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    StocksService stocksService;

    @Test
    void getStockCodes_returnsOk() {
        Mockito.when(stocksService.getStocksCodeListByNational("KR"))
                .thenReturn(List.of("005930"));

        webTestClient.get().uri("/api/stocks/code/KR")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.codes[0]").isEqualTo("005930");
    }
}


