package com.ordermentum.microsrc.exception;

import com.ordermentum.microsrc.comp.Product;

import java.math.BigDecimal;

public class InsufficientChangeException extends RuntimeException{

    public InsufficientChangeException(BigDecimal change, Product p){
        super("No enough change (" + change + ") for " + p.getName());
    }
}
