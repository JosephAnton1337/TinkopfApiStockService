package com.rudnev.tinkopfapistockservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TinkoffApiStockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffApiStockServiceApplication.class, args);
    }

}
