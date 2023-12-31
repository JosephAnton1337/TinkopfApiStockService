package com.rudnev.tinkopfapistockservice.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class Stock {
    String ticker;
    String figi;
    String name;
    String type;
    Currency currency;
    String sourse;
}
