package com.ordermentum.microsrc.impl;

import com.ordermentum.microsrc.comp.Coin;
import com.ordermentum.microsrc.comp.Product;
import com.ordermentum.microsrc.exception.InsufficientChangeException;
import com.ordermentum.microsrc.exception.InsufficientPaymentException;
import com.ordermentum.microsrc.exception.InvalidProductException;
import com.ordermentum.microsrc.exception.InvalidStockQuantityException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class VendingMachine {

    protected List<Coin> coinsInserted;
    protected HashMap<Coin, Integer> changeAvailable; //coin and quantity
    protected HashMap<String, Product> productList; //product and quantity

    public VendingMachine(){
        coinsInserted = new ArrayList<>();
        changeAvailable = new HashMap<>();
        productList = new HashMap<>();
    }

    public Product getProductByCode(String code){
        return productList.get(code);
    }

    public void restockItem(String code, Product product, int stock){
        if (stock <= 0)
            throw new InvalidStockQuantityException(stock);

        if (getProductByCode(code) == null) {
            product.setStock(stock);
            productList.put(code, product);
        }
        else{
            Product currItem = productList.get(code);
            int newStock = currItem.getStock() + stock;
            currItem.setStock(newStock);
            productList.put(code, currItem);
        }
    }

    public Integer getChangeStock(Coin coin){
        return changeAvailable.get(coin);
    }

    public void restockCoins(Coin coin, int stock){
        if (stock <= 0)
            throw new InvalidStockQuantityException(stock);

        if (changeAvailable.get(coin) == null)
            changeAvailable.put(coin, stock);
        else {
            int newStock = changeAvailable.get(coin) + stock;
            changeAvailable.put(coin, newStock);
        }
    }

    public abstract void insertCoin(String input);

    public BigDecimal getTotalAmountInserted(){
        BigDecimal total = BigDecimal.ZERO;
        for(Coin coin : coinsInserted){
            total = total.add(coin.getValue());
        }
        return total;
    }

    public abstract boolean hasAvailableChange(Product product);

    public BigDecimal getChangeValue(Product product){
        return getTotalAmountInserted().subtract(product.getCost());
    }

    public BigDecimal getChangeValueClearCoinsInserted(Product product){
        BigDecimal change = getChangeValue(product);
        this.coinsInserted = new ArrayList<>();
        return change;
    }

    public void buyItem(String code){
        Product product = getProductByCode(code);

        if (product == null)
            throw new InvalidProductException(code);

        BigDecimal change = getChangeValue(product);

        if (change.compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientPaymentException(getTotalAmountInserted());

        if (hasAvailableChange(product)){
            deductItem(code);
            deductChangeList(product);
        }
    }

    public abstract List<Coin> getChange(Product product);

    public void cancelTransaction(){
        for (Coin c : coinsInserted){
            changeAvailable.put(c, changeAvailable.get(c) - 1);
        }

        this.coinsInserted = new ArrayList<>();
    }

    public HashMap<String, Product> getAffordProducts(){
        HashMap<String, Product> prodList = new HashMap<>();
        for (String code : productList.keySet()) {
            Product p = getProductByCode(code);
            if (getTotalAmountInserted().compareTo(p.getCost()) >= 0) {
                prodList.put(code, p);
            }
        }
        return prodList;
    }

    public void deductItem(String code){
        Product p = getProductByCode(code);
        p.deductStock();
        if (p.getStock() == 0)
            productList.remove(code);
        else
            productList.put(code, p);
    }

    public void deductChangeList(Product product){
        List<Coin> changeList = getChange(product);
        for (Coin c : changeList){
            int newStock = changeAvailable.get(c) - 1;
            if (newStock == 0)
                changeAvailable.remove(c);
            else
                changeAvailable.put(c, changeAvailable.get(c) - 1);
        }
    }

    public boolean coinAvailableFromChange(Coin coin, BigDecimal balance){
        if (changeAvailable.get(coin) == null)
            return false;
        if (balance.compareTo(coin.getValue()) < 0)
            return false;
        return true;
    }


    //getters
    public HashMap<String, Product> getProductList() {
        return productList;
    }

    public HashMap<Coin, Integer> getChangeAvailable() {
        return changeAvailable;
    }

    public List<Coin> getCoinsInserted() {
        return coinsInserted;
    }

    public void setCoinsInserted(List<Coin> coinsInserted) {
        this.coinsInserted = coinsInserted;
    }

    public void setChangeAvailable(HashMap<Coin, Integer> changeAvailable) {
        this.changeAvailable = changeAvailable;
    }

    public void setProductList(HashMap<String, Product> productList) {
        this.productList = productList;
    }
}
