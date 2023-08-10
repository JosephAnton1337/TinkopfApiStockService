package com.rudnev.tinkopfapistockservice.controller;

import com.rudnev.tinkopfapistockservice.dto.StocksDto;
import com.rudnev.tinkopfapistockservice.dto.TickersDto;
import com.rudnev.tinkopfapistockservice.model.Stock;
import com.rudnev.tinkopfapistockservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class StockController {

    private final StockService stockService;


    @GetMapping("/stocks/{ticker}")
    public Stock getStock(String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/stocks/getStockByTicker")
    public StocksDto getStockByTickers(@RequestBody TickersDto tickersDto) {

        return stockService.getStockByTickers(tickersDto);
    }
}
