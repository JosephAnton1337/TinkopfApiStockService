package com.rudnev.tinkopfapistockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@AllArgsConstructor
@Value
public class StocksPricesDto {
    private List<StockPrice> prices;
}
