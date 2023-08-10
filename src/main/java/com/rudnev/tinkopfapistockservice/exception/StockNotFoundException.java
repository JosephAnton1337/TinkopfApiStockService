package com.rudnev.tinkopfapistockservice.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String message) {
        super(message);
    }
}
