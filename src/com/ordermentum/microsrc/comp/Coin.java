package com.ordermentum.microsrc.comp;

import java.math.BigDecimal;

public enum Coin {
    TEN_CENTS(new BigDecimal(.10)), TWENTY_CENTS(new BigDecimal(.20)), FIFTY_CENTS(new BigDecimal(.50)),
    ONE_DOLLAR(new BigDecimal(1)), TWO_DOLLARS(new BigDecimal(2));

    private BigDecimal value;

    Coin (BigDecimal value){
        this.value = value;
    }

    public BigDecimal getValue(){
        return value;
    }
}
