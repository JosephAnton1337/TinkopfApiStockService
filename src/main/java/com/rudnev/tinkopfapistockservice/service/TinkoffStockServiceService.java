package com.rudnev.tinkopfapistockservice.service;

import com.rudnev.tinkopfapistockservice.dto.StocksDto;
import com.rudnev.tinkopfapistockservice.dto.TickersDto;
import com.rudnev.tinkopfapistockservice.exception.StockNotFoundException;
import com.rudnev.tinkopfapistockservice.model.Currency;
import com.rudnev.tinkopfapistockservice.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TinkoffStockServiceService implements StockService {

    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentByTicker(String ticker) {
        var context = openApi.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStockByTicker(@PathVariable String ticker) {
        var completableFuture = getMarketInstrumentByTicker(ticker);
        var list = completableFuture.join().getInstruments();
        if (list.isEmpty()) {
            throw new StockNotFoundException(String.format("Stock %s not found", ticker));
        }
        var item = list.get(0);
        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                "TINKOFF");
    }

    @Override
    public StocksDto getStockByTickers(TickersDto tickers) {
        List<CompletableFuture<MarketInstrumentList>> marketInstruments = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> marketInstruments.add(getMarketInstrumentByTicker(ticker)));
        List<Stock> stocks = marketInstruments.stream()
                .map(CompletableFuture::join)
                .map(i -> {
                    if (!i.getInstruments().isEmpty()) {
                        return i.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF"
                )).toList();
        return new StocksDto(stocks);

//        tickers.getTickers().forEach(this::getStockByTicker);

    }
}
