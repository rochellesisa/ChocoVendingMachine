package com.ordermentum.microsrc.comp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {

    private String name;
    private String key;
    private BigDecimal cost;

    public Product() {
    }

    public Product(String name, String key, BigDecimal cost) {
        this.name = name;
        this.key = key;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getCost() {
        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

}
