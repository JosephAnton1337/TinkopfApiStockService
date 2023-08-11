package com.rudnev.tinkopfapistockservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAutoConfiguration
@EnableAsync
public class TinkoffApiStockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffApiStockServiceApplication.class, args);
    }

}
