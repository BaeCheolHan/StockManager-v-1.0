package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.service.ChartService;
import com.my.stock.stockmanager.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@WebMvcTest(controllers = StockController.class, excludeAutoConfiguration = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@ImportAutoConfiguration
class StockControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ChartService chartService;

    @MockBean
    StockService stockService;

    @Test
    void getChart_returnsOk() throws Exception {
        Mockito.when(chartService.getDailyChartData("D", "KR", "005930")).thenReturn(List.of());

        webTestClient.get().uri("/api/stock/chart/D/KR/005930")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo("SUCCESS");
    }
}


