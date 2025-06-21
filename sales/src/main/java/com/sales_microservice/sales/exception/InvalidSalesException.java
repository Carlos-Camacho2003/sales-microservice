package com.sales_microservice.sales.exception;

public class InvalidSalesException extends RuntimeException {
    public InvalidSalesException(String message) {
        super(message);
    }
}
