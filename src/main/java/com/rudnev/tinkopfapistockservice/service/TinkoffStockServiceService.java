package com.rudnev.tinkopfapistockservice.service;

import com.rudnev.tinkopfapistockservice.dto.FigiesDto;
import com.rudnev.tinkopfapistockservice.dto.StockPrice;
import com.rudnev.tinkopfapistockservice.dto.StocksDto;
import com.rudnev.tinkopfapistockservice.dto.StocksPricesDto;
import com.rudnev.tinkopfapistockservice.dto.TickersDto;
import com.rudnev.tinkopfapistockservice.exception.StockNotFoundException;
import com.rudnev.tinkopfapistockservice.model.Currency;
import com.rudnev.tinkopfapistockservice.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
    }

    public StockPrice getPrice(String figi) {
        var orderBook = openApi.getMarketContext().getMarketOrderbook(figi, 0).join().get();
        return new StockPrice(figi, orderBook.getLastPrice().doubleValue());
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        log.info("Getting price {} from Tinkoff", figi);
        return openApi.getMarketContext().getMarketOrderbook(figi, 0);
    }

    @Override
    public StocksPricesDto getPrices(FigiesDto figiesDto) {
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
        var listLastPrices = orderBooks.stream()
                .map(CompletableFuture::join)
                .map(orderBooking -> orderBooking.orElseThrow(() -> new StockNotFoundException("Stock not found")))
                .map(order -> new StockPrice(
                        order.getFigi(),
                        order.getLastPrice().doubleValue()
                )).collect(Collectors.toList());

        return new StocksPricesDto(listLastPrices);
    }
}
