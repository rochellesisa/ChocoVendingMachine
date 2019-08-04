package com.ordermentum.microsrc.exception;

public class InvalidProductException extends RuntimeException {

    public InvalidProductException(String code){
        super("Invalid product code: " + code);
    }
}
