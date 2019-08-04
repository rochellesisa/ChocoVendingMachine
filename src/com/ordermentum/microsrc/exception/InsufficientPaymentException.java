package com.ordermentum.microsrc.exception;

import com.ordermentum.microsrc.comp.Product;

import java.math.BigDecimal;

public class InsufficientPaymentException extends RuntimeException {

    public InsufficientPaymentException(BigDecimal payment){
        super("Insufficient payment made: $" + payment.setScale(2));
    }
}
