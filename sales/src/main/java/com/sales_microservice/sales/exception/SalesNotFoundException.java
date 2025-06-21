package com.sales_microservice.sales.exception;

public class SalesNotFoundException extends RuntimeException {
    public SalesNotFoundException(String message) {
        super(message);
    }
}
