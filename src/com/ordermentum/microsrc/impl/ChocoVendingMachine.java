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
                changeAvailable.put(Coin.TEN_CENTS, changeAvailable.get(Coin.TEN_CENTS) == null ? 1 : changeAvailable.get(Coin.TEN_CENTS) + 1);
                break;
            case Constants.TWENTY_CENT:
                coinsInserted.add(Coin.TWENTY_CENTS);
                changeAvailable.put(Coin.TWENTY_CENTS, changeAvailable.get(Coin.TWENTY_CENTS) == null ? 1 : changeAvailable.get(Coin.TWENTY_CENTS) + 1);
                break;
            case Constants.FIFTY_CENT:
                coinsInserted.add(Coin.FIFTY_CENTS);
                changeAvailable.put(Coin.FIFTY_CENTS, changeAvailable.get(Coin.FIFTY_CENTS) == null ? 1 : changeAvailable.get(Coin.FIFTY_CENTS) + 1);
                break;
            case Constants.ONE_DOLLAR:
                coinsInserted.add(Coin.ONE_DOLLAR);
                changeAvailable.put(Coin.ONE_DOLLAR, changeAvailable.get(Coin.ONE_DOLLAR) == null ? 1 : changeAvailable.get(Coin.ONE_DOLLAR) + 1);
                break;
            case Constants.TWO_DOLLAR:
                coinsInserted.add(Coin.TWO_DOLLARS);
                changeAvailable.put(Coin.TWO_DOLLARS, changeAvailable.get(Coin.TWO_DOLLARS) == null ? 1 : changeAvailable.get(Coin.TWO_DOLLARS) + 1);
                break;
            default:
                throw new InvalidInputException("Invalid amount entered", input);
        }
    }

    @Override
    public List<Coin> getChange(Product product) {
        List<Coin> changeList = new ArrayList<>();
        BigDecimal balance = getChangeValue(product);
        int oneDollarQty = changeAvailable.get(Coin.ONE_DOLLAR) == null ? 0 : changeAvailable.get(Coin.ONE_DOLLAR);
        int twoDollarQty = changeAvailable.get(Coin.TWO_DOLLARS) == null ? 0 : changeAvailable.get(Coin.TWO_DOLLARS);
        int fiftyCQty = changeAvailable.get(Coin.FIFTY_CENTS) == null ? 0 : changeAvailable.get(Coin.FIFTY_CENTS);
        int twentyCQty = changeAvailable.get(Coin.TWENTY_CENTS) == null ? 0 : changeAvailable.get(Coin.TWENTY_CENTS);
        int tenCQty = changeAvailable.get(Coin.TEN_CENTS) == null ? 0 : changeAvailable.get(Coin.TEN_CENTS);

        while (balance.compareTo(BigDecimal.ZERO) > 0){
            if (coinAvailableFromChange(Coin.TWO_DOLLARS, balance) && twoDollarQty > 0){
                balance = balance.subtract(Coin.TWO_DOLLARS.getValue());
                changeList.add(Coin.TWO_DOLLARS);
                twoDollarQty--;
                continue;
            } else if (coinAvailableFromChange(Coin.ONE_DOLLAR, balance) && oneDollarQty > 0) {
                balance = balance.subtract(Coin.ONE_DOLLAR.getValue());
                changeList.add(Coin.ONE_DOLLAR);
                oneDollarQty--;
                continue;
            } else if (coinAvailableFromChange(Coin.FIFTY_CENTS, balance) && fiftyCQty > 0) {
                balance = balance.subtract(Coin.FIFTY_CENTS.getValue());
                changeList.add(Coin.FIFTY_CENTS);
                fiftyCQty--;
                continue;
            } else if (coinAvailableFromChange(Coin.TWENTY_CENTS, balance) && twentyCQty > 0) {
                balance = balance.subtract(Coin.TWENTY_CENTS.getValue());
                changeList.add(Coin.TWENTY_CENTS);
                twentyCQty--;
                continue;
            } else if (coinAvailableFromChange(Coin.TEN_CENTS, balance) && tenCQty > 0) {
                balance = balance.subtract(Coin.TEN_CENTS.getValue());
                changeList.add(Coin.TEN_CENTS);
                tenCQty--;
                continue;
            } else {
                throw new InsufficientChangeException(getChangeValue(product), product);
            }
        }
        return changeList;
    }


    @Override
    public boolean hasAvailableChange(Product product) {
        try {
            getChange(product);
        } catch (InsufficientChangeException e){
            throw e;
        }
        return true;
    }


}
