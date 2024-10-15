package com.my.stock.stockmanager.config;

import feign.*;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import javax.net.ssl.SSLContext;

@Configuration
@EnableFeignClients("com.my.stock.stockmanager.api")
public class OpenFeignConfig {

    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegister() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }

    @Bean
    public Client feignClient() throws Exception {
        // 신뢰할 수 있는 SSLContext 설정
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();

        return new Client.Default(sslContext.getSocketFactory(), new NoopHostnameVerifier());
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(5000, 30000);  // 연결 타임아웃 5초, 읽기 타임아웃 30초
    }

    @Bean
    public Retryer retryer() {
        // Feign의 기본 Retryer를 무효화하거나 필요에 맞게 설정
        return new Retryer.Default(100, 1000, 3); // 최대 3번의 리다이렉션 허용
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("Connection", "close");  // 필요한 경우 연결 종료 헤더 추가
        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;  // 전체 요청 및 응답 로깅
    }
}
