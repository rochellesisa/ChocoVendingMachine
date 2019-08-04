package com.ordermentum.microsrc.exception;

public class InvalidStockQuantityException extends RuntimeException{

    public InvalidStockQuantityException(int stock){
        super("Invalid stock inputted: " + stock);
    }
}
