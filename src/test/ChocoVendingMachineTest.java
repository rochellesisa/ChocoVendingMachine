package test;

import com.ordermentum.microsrc.comp.Coin;
import com.ordermentum.microsrc.comp.Product;
import com.ordermentum.microsrc.exception.InsufficientChangeException;
import com.ordermentum.microsrc.exception.InsufficientPaymentException;
import com.ordermentum.microsrc.exception.InvalidInputException;
import com.ordermentum.microsrc.exception.InvalidProductException;
import com.ordermentum.microsrc.impl.ChocoVendingMachine;
import com.ordermentum.microsrc.impl.VendingMachine;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ChocoVendingMachineTest {

    private static VendingMachine vm;

    @Before
    public void initTest(){
        vm = new ChocoVendingMachine();
    }

    //insertCoin
    @Test
    public void insertTenCents(){
        vm.insertCoin("10c");
        assertThat(vm.getCoinsInserted(), containsInAnyOrder(Coin.TEN_CENTS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.TEN_CENTS));
    }

    @Test
    public void insertTwentyCents(){
        vm.insertCoin("20c");
        assertThat(vm.getCoinsInserted(), containsInAnyOrder(Coin.TWENTY_CENTS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.TWENTY_CENTS));

    }

    @Test
    public void insertFiftyCents(){
        vm.insertCoin("50c");
        assertThat(vm.getCoinsInserted(), containsInAnyOrder(Coin.FIFTY_CENTS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.FIFTY_CENTS));
    }

    @Test
    public void insertOneDollar(){
        vm.insertCoin("$1");
        assertThat(vm.getCoinsInserted(), containsInAnyOrder(Coin.ONE_DOLLAR));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.ONE_DOLLAR));
    }

    @Test
    public void insertTwoDollars(){
        vm.insertCoin("$2");
        assertThat(vm.getCoinsInserted(), containsInAnyOrder(Coin.TWO_DOLLARS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.TWO_DOLLARS));
    }

    @Test
    public void insertMultipleCoins(){
        vm.insertCoin("10c");
        vm.insertCoin("20c");
        vm.insertCoin("50c");
        vm.insertCoin("$1");
        vm.insertCoin("$1");
        vm.insertCoin("$2");
        assertThat(vm.getCoinsInserted(), containsInAnyOrder(Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.FIFTY_CENTS, Coin.ONE_DOLLAR, Coin.ONE_DOLLAR, Coin.TWO_DOLLARS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.TEN_CENTS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.TWENTY_CENTS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.FIFTY_CENTS));
        assertEquals(new Integer(2), vm.getChangeAvailable().get(Coin.ONE_DOLLAR));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.TWO_DOLLARS));

    }

    @Test(expected = InvalidInputException.class)
    public void insertInvalidCoin(){
        vm.insertCoin("5c");
    }

    //getChange
    @Test
    public void changeForTwoDollars(){
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 3);
        Product p = new Product("Organic Raw", BigDecimal.valueOf(2.00));
        List<Coin> changeList = vm.getChange(p);
        assertThat(changeList, containsInAnyOrder(Coin.TWO_DOLLARS));
    }

    @Test
    public void changeForOneDollar(){
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 2);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 2);
        Product p = new Product("Organic Raw", BigDecimal.valueOf(2.00));
        List<Coin> changeList = vm.getChange(p);
        assertThat(changeList, containsInAnyOrder(Coin.ONE_DOLLAR));
    }

    @Test
    public void changeForFiftyCents(){
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getChangeAvailable().put(Coin.FIFTY_CENTS, 2);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 1);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 1);
        Product p = new Product("Caramel", BigDecimal.valueOf(2.50));
        List<Coin> changeList = vm.getChange(p);
        assertThat(changeList, containsInAnyOrder(Coin.FIFTY_CENTS));
    }

    @Test
    public void changeForTwentyCents(){
        vm.getCoinsInserted().add(Coin.TEN_CENTS);
        vm.getCoinsInserted().add(Coin.TEN_CENTS);
        vm.getCoinsInserted().add(Coin.TEN_CENTS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getChangeAvailable().put(Coin.TWENTY_CENTS, 2);
        vm.getChangeAvailable().put(Coin.TEN_CENTS, 3);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 3);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 3);
        Product p = new Product("Hazelnut", BigDecimal.valueOf(3.10));
        List<Coin> changeList = vm.getChange(p);
        assertThat(changeList, containsInAnyOrder(Coin.TWENTY_CENTS));
    }

    @Test
    public void changeForTenCents(){
        vm.getCoinsInserted().add(Coin.TWENTY_CENTS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getChangeAvailable().put(Coin.TEN_CENTS, 2);
        vm.getChangeAvailable().put(Coin.TWENTY_CENTS, 1);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 3);
        Product p = new Product("Hazelnut", BigDecimal.valueOf(3.10));
        List<Coin> changeList = vm.getChange(p);
        assertThat(changeList, containsInAnyOrder(Coin.TEN_CENTS));
    }

    @Test
    public void multipleCoinsChange(){
        vm.getCoinsInserted().add(Coin.TEN_CENTS);
        vm.getCoinsInserted().add(Coin.TWENTY_CENTS);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getChangeAvailable().put(Coin.FIFTY_CENTS, 3);
        vm.getChangeAvailable().put(Coin.TWENTY_CENTS, 2);
        vm.getChangeAvailable().put(Coin.TEN_CENTS, 3);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 1);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 1);
        Product p = new Product("Caramel", BigDecimal.valueOf(2.50));
        List<Coin> changeList = vm.getChange(p);
        assertThat(changeList, containsInAnyOrder(Coin.FIFTY_CENTS, Coin.TWENTY_CENTS, Coin.TEN_CENTS));
    }

    //hasAvailableChange
    @Test(expected = InsufficientChangeException.class)
    public void insufficientChange(){
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 1);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 1);
        Product p = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.getChange(p);
    }
//
    @Test
    public void enoughChange(){
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getChangeAvailable().put(Coin.FIFTY_CENTS, 2);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 1);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 1);
        Product p = new Product("Caramel", BigDecimal.valueOf(2.50));
        List<Coin> changeList = vm.getChange(p);
        assertTrue(vm.hasAvailableChange(p));
    }

    @Test(expected = InsufficientChangeException.class)
    public void noEnoughChange(){
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 1);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 1);
        Product p = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.hasAvailableChange(p);
    }

    //deductChangeList
    @Test
    public void deductChangeListToZero(){
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 1);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 1);
        vm.getChangeAvailable().put(Coin.FIFTY_CENTS, 1);
        Product p = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.deductChangeList(p);
        assertNull(vm.getChangeAvailable().get(Coin.FIFTY_CENTS));
    }

    @Test
    public void deductChangeList(){
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 1);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 1);
        vm.getChangeAvailable().put(Coin.FIFTY_CENTS, 3);
        Product p = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.deductChangeList(p);
        assertEquals(new Integer(2), vm.getChangeAvailable().get(Coin.FIFTY_CENTS));
    }

    //buyItem
    @Test(expected = InvalidProductException.class)
    public void buyNonExistingItem(){
        vm.buyItem("1004");
    }

    @Test(expected = InsufficientPaymentException.class)
    public void buyWithInsufficientPayment(){
        vm.getProductList().put("1004", new Product("Caramel", BigDecimal.valueOf(2.50), 3));
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.buyItem("1004");
    }

    @Test(expected = InsufficientChangeException.class)
    public void buyWithNoEnoughChange(){
        vm.getProductList().put("1004", new Product("Caramel", BigDecimal.valueOf(2.50), 3));
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.buyItem("1004");
    }

    @Test
    public void buyItemWithCorrectConditions(){
        vm.getProductList().put("1004", new Product("Caramel", BigDecimal.valueOf(2.50), 3));
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 2);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 2);
        vm.getChangeAvailable().put(Coin.TWENTY_CENTS, 2);
        vm.getChangeAvailable().put(Coin.TEN_CENTS, 2);
        vm.buyItem("1004");
        assertEquals(2, vm.getProductList().get("1004").getStock());
        assertEquals(new Integer(2), vm.getChangeAvailable().get(Coin.TWO_DOLLARS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.ONE_DOLLAR));
        assertNull(vm.getChangeAvailable().get(Coin.TWENTY_CENTS));
        assertEquals(new Integer(1), vm.getChangeAvailable().get(Coin.ONE_DOLLAR));
    }

}
