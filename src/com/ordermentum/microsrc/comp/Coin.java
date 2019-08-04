package com.ordermentum.microsrc.comp;

import java.math.BigDecimal;

public enum Coin {
    TEN_CENTS(BigDecimal.valueOf(.10)), TWENTY_CENTS(BigDecimal.valueOf(.20)), FIFTY_CENTS(BigDecimal.valueOf(.50)),
    ONE_DOLLAR(BigDecimal.valueOf(1)), TWO_DOLLARS(BigDecimal.valueOf(2));

    private BigDecimal value;

    Coin (BigDecimal value){
        this.value = value;
    }

    public BigDecimal getValue(){
        return value;
    }
}
