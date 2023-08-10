package com.rudnev.tinkopfapistockservice.service;

import com.rudnev.tinkopfapistockservice.dto.StocksDto;
import com.rudnev.tinkopfapistockservice.dto.TickersDto;
import com.rudnev.tinkopfapistockservice.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksDto getStockByTickers(TickersDto tickers);
}
