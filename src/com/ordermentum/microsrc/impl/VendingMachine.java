package com.ordermentum.microsrc.impl;

import com.ordermentum.microsrc.comp.Coin;
import com.ordermentum.microsrc.comp.Product;
import com.ordermentum.microsrc.exception.InsufficientChangeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class VendingMachine {

    List<Coin> coinsInserted;
    HashMap<Coin, Integer> changeAvailable; //coin and quantity
    HashMap<Product, Integer> productList; //product and quantity

    public VendingMachine(){
        coinsInserted = new ArrayList<>();
        changeAvailable = new HashMap<>();
        productList = new HashMap<>();
    }

    public void restockItem(Product product, int stock){
        if (productList.get(product) == null)
            productList.put(product, stock);
        else{
            int currStock = productList.get(product);
            productList.put(product, stock + currStock);
        }
    }

    public void restockItem(Coin coin, int stock){
        changeAvailable.put(coin, stock);
    }

    public abstract void insertCoin(String input);

    public BigDecimal getTotalAmountInserted(){
        BigDecimal total = new BigDecimal(0);
        for(Coin coin : coinsInserted){
            total = total.add(coin.getValue());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean hasCorrectAmount(Product product){
        if (getTotalAmountInserted().compareTo(product.getCost()) >= 0)
            return true;
        return false;
    }

    public abstract boolean hasAvailableChange(Product product);

    public void buyItem(Product product){
        BigDecimal change = getChangeValue(product);

        if (hasAvailableChange(product)){
            deductItem(product);
            deductChangeList(product);
        } else {
            throw new InsufficientChangeException(change, product);
        }
    }

    public abstract List<Coin> getChange(Product product);

    public BigDecimal getChangeValue(Product product){
        return getTotalAmountInserted().subtract(product.getCost()).setScale(2, RoundingMode.HALF_UP);
    }

    public List<Coin> getCoinsInserted() {
        return coinsInserted;
    }

    public void setCoinsInserted(List<Coin> coinsInserted) {
        this.coinsInserted = coinsInserted;
    }

    public HashMap<Coin, Integer> getChangeAvailable() {
        return changeAvailable;
    }

    public void setChangeAvailable(HashMap<Coin, Integer> changeAvailable) {
        this.changeAvailable = changeAvailable;
    }

    public HashMap<Product, Integer> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<Product, Integer> productList) {
        this.productList = productList;
    }

    public BigDecimal getLeastPrice(){
        BigDecimal price = null;
        for (Product p : productList.keySet()){
            if (price == null || price.compareTo(p.getCost()) <= 0)
                return p.getCost().setScale(2);
        }
        return new BigDecimal(0);
    }

    public void cancelTransaction(){
        this.coinsInserted = Collections.emptyList();

        for (Coin c : coinsInserted){
            changeAvailable.put(c, changeAvailable.get(c) - 1);
        }
    }

    public List<Product> getAffordProducts(){
        List<Product> prodList = new ArrayList<>();
        for (Product p : productList.keySet()) {
            if (productList.get(p) > 0 && getTotalAmountInserted().compareTo(p.getCost()) >= 0) {
                prodList.add(p);
            }
        }
        return prodList;
    }

    public void deductItem(Product product){
        int stock = productList.get(product);
        productList.put(product, stock - 1);
    }

    public void deductChangeList(Product product){
        for (Coin c : getChange(product)){
            changeAvailable.put(c, changeAvailable.get(c) - 1);
        }
    }

}
