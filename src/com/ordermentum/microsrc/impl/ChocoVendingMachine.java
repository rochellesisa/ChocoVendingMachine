package com.ordermentum.microsrc.impl;

import com.ordermentum.microsrc.comp.Coin;
import com.ordermentum.microsrc.comp.Product;
import com.ordermentum.microsrc.exception.InsufficientChangeException;
import com.ordermentum.microsrc.exception.InvalidInputException;
import com.ordermentum.microsrc.util.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ChocoVendingMachine extends VendingMachine {

    @Override
    public void insertCoin(String input) {
        switch (input) {
            case Constants.TEN_CENT:
                coinsInserted.add(Coin.TEN_CENTS);
                changeAvailable.put(Coin.TEN_CENTS, changeAvailable.get(Coin.TEN_CENTS) + 1);
                break;
            case Constants.TWENTY_CENT:
                coinsInserted.add(Coin.TWENTY_CENTS);
                changeAvailable.put(Coin.TWENTY_CENTS, changeAvailable.get(Coin.TWENTY_CENTS) + 1);
                break;
            case Constants.FIFTY_CENT:
                coinsInserted.add(Coin.FIFTY_CENTS);
                changeAvailable.put(Coin.FIFTY_CENTS, changeAvailable.get(Coin.FIFTY_CENTS) + 1);
                break;
            case Constants.ONE_DOLLAR:
                coinsInserted.add(Coin.ONE_DOLLAR);
                changeAvailable.put(Coin.ONE_DOLLAR, changeAvailable.get(Coin.ONE_DOLLAR) + 1);
                break;
            case Constants.TWO_DOLLAR:
                coinsInserted.add(Coin.TWO_DOLLARS);
                changeAvailable.put(Coin.TWO_DOLLARS, changeAvailable.get(Coin.TWO_DOLLARS) + 1);
                break;
            default:
                throw new InvalidInputException("Invalid amount entered", input);
        }
    }

    @Override
    public boolean hasAvailableChange(Product product) {
        try {
            getChange(product);
        } catch (InsufficientChangeException e){
            return false;
        }
        return true;
    }

    @Override
    public List<Coin> getChange(Product product) {
        List<Coin> changeList = new ArrayList<>();
        BigDecimal balance = getChangeValue(product);

        while (balance.compareTo(BigDecimal.ZERO) > 0){
            if (balance.compareTo(Coin.TWO_DOLLARS.getValue()) >= 0 && changeAvailable.get(Coin.TWO_DOLLARS) > 0){
                balance = balance.subtract(Coin.TWO_DOLLARS.getValue()).setScale(2, RoundingMode.HALF_UP);
                changeList.add(Coin.TWO_DOLLARS);
                continue;
            } else if (balance.compareTo(Coin.ONE_DOLLAR.getValue()) >= 0 && changeAvailable.get(Coin.ONE_DOLLAR) > 0) {
                balance = balance.subtract(Coin.ONE_DOLLAR.getValue()).setScale(2, RoundingMode.HALF_UP);
                changeList.add(Coin.ONE_DOLLAR);
                continue;
            } else if (balance.compareTo(Coin.FIFTY_CENTS.getValue()) >= 0 && changeAvailable.get(Coin.FIFTY_CENTS) > 0) {
                balance = balance.subtract(Coin.FIFTY_CENTS.getValue()).setScale(2, RoundingMode.HALF_UP);
                changeList.add(Coin.FIFTY_CENTS);
                continue;
            } else if (balance.compareTo(Coin.TWENTY_CENTS.getValue()) >= 0 && changeAvailable.get(Coin.TWENTY_CENTS) > 0) {
                balance = balance.subtract(Coin.TWENTY_CENTS.getValue()).setScale(2, RoundingMode.HALF_UP);
                changeList.add(Coin.TWENTY_CENTS);
                continue;
            } else if (balance.compareTo(Coin.TEN_CENTS.getValue()) >= 0 && changeAvailable.get(Coin.TEN_CENTS) > 0) {
                balance = balance.subtract(Coin.TEN_CENTS.getValue()).setScale(2, RoundingMode.HALF_UP);
                changeList.add(Coin.TEN_CENTS);
                continue;
            } else {
                throw new InsufficientChangeException(getChangeValue(product), product);
            }
        }
        return changeList;
    }




}
