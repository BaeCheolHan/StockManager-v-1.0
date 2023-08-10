package com.my.stock.stockmanager.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.my.stock.stockmanager.api")
public class OpenFeignConfig {
}
