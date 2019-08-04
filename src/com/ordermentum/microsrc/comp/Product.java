package com.ordermentum.microsrc.comp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {

    private String name;
    private int stock;
    private BigDecimal cost;

    public Product() {
    }

    public Product(String name, BigDecimal cost, int stock) {
        this.name = name;
        this.cost = cost;
        this.stock = stock;
    }

    public Product(String name, BigDecimal cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void deductStock(){
        stock -= 1;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product prod = (Product) obj;
        return name == prod.name &&
                stock == prod.stock &&
                cost.equals(prod.cost);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + stock + cost.hashCode();
    }
}
