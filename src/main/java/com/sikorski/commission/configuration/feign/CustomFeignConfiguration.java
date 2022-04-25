package com.sikorski.commission.configuration.feign;

import com.sikorski.commission.infrastructure.fx.ExchangeRateClient;
import feign.Feign;
import feign.Request;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CustomFeignConfiguration {

    @Value("${feign.client.fx-client.url}")
    String fxClientUrl;

    @Value("${feign.client.fx-client.connectTimeout}")
    Integer connectTimeout;

    @Value("${feign.client.fx-client.readTimeout}")
    Integer readTimeout;

    @Bean
    ExchangeRateClient exchangeRateClient() {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .options(new Request.Options(connectTimeout, TimeUnit.MILLISECONDS,
                        readTimeout, TimeUnit.MILLISECONDS, true))
                .target(ExchangeRateClient.class, fxClientUrl);
    }
}