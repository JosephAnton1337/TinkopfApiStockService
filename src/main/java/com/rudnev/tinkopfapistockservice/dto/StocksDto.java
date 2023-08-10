package com.rudnev.tinkopfapistockservice.dto;

import com.rudnev.tinkopfapistockservice.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StocksDto {
    List<Stock> stocks;
}
