package test;

import com.ordermentum.microsrc.comp.Coin;
import com.ordermentum.microsrc.comp.Product;
import com.ordermentum.microsrc.exception.InvalidStockQuantityException;
import com.ordermentum.microsrc.impl.VendingMachine;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class VendingMachineTest {

    private static VendingMachine vm;

    @Before
    public void initTest() {
        vm = createVending();
    }

    //getProductByCode()
    @Test
    public void getInvalidProductByCode() {
        Product p = vm.getProductByCode("1004");
        assertNull(p);
    }

    @Test
    public void getValidProductByCode() {
        vm.getProductList().put("1004", new Product("Caramel", BigDecimal.valueOf(2.50), 5));
        Product p = vm.getProductByCode("1004");
        assertNotNull(p);
    }

    //restockItem
    @Test(expected = InvalidStockQuantityException.class)
    public void inputNegativeItemStock() {
        Product caramel = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.restockItem("1004", caramel, -1);

    }

    @Test(expected = InvalidStockQuantityException.class)
    public void inputZeroItemStock() {
        Product caramel = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.restockItem("1004", caramel, 0);
    }

    @Test
    public void putNewItem() {
        Product caramel = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.restockItem("1004", caramel, 5);
        assertEquals("Caramel", vm.getProductByCode("1004").getName());
        assertEquals(5, vm.getProductByCode("1004").getStock());
        assertEquals(BigDecimal.valueOf(2.50), vm.getProductByCode("1004").getCost());
    }

    @Test
    public void addStockToExistingItem() {
        Product caramel = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.restockItem("1004", caramel, 5);
        vm.restockItem("1004", caramel, 3);
        assertEquals(8, vm.getProductByCode("1004").getStock());
    }

    //getChangeStock
    @Test
    public void getInvalidChange() {
        assertNull(vm.getChangeStock(Coin.FIFTY_CENTS));
    }

    @Test
    public void getValidChange() {
        vm.getChangeAvailable().put(Coin.FIFTY_CENTS, 50);
        assertEquals(new Integer(50), vm.getChangeStock(Coin.FIFTY_CENTS));
    }

    //restockCoins
    @Test(expected = InvalidStockQuantityException.class)
    public void inputNegativeCoinStock() {
        vm.restockCoins(Coin.FIFTY_CENTS, -3);
    }

    @Test(expected = InvalidStockQuantityException.class)
    public void inputZeroCoinStock() {
        vm.restockCoins(Coin.FIFTY_CENTS, 0);
    }

    @Test
    public void addNewCoin() {
        vm.restockCoins(Coin.FIFTY_CENTS, 50);
        assertEquals(new Integer(50), vm.getChangeStock(Coin.FIFTY_CENTS));
    }

    @Test
    public void addCoinInventory() {
        vm.restockCoins(Coin.TEN_CENTS, 10);
        vm.restockCoins(Coin.TEN_CENTS, 15);
        assertEquals(new Integer(25), vm.getChangeStock(Coin.TEN_CENTS));

    }

    //getTotalAmountInserted
    @Test
    public void insertTenCents() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.TEN_CENTS);
        vm.setCoinsInserted(coinsInserted);
        assertEquals(BigDecimal.valueOf(.10), vm.getTotalAmountInserted());
    }

    @Test
    public void insertTwentyCents() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.TWENTY_CENTS);
        vm.setCoinsInserted(coinsInserted);
        assertEquals(BigDecimal.valueOf(.20), vm.getTotalAmountInserted());
    }

    @Test
    public void insertFiftyCents() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.FIFTY_CENTS);
        vm.setCoinsInserted(coinsInserted);
        assertEquals(BigDecimal.valueOf(.50), vm.getTotalAmountInserted());
    }


    @Test
    public void insertOneDollarCents() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.ONE_DOLLAR);
        vm.setCoinsInserted(coinsInserted);
        assertEquals(BigDecimal.valueOf(1), vm.getTotalAmountInserted());
    }

    @Test
    public void insertTwoDollarsCents() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.TWO_DOLLARS);
        vm.setCoinsInserted(coinsInserted);
        assertEquals(BigDecimal.valueOf(2), vm.getTotalAmountInserted());
    }

    @Test
    public void computeForTotalCoinsInserted() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.TEN_CENTS);
        coinsInserted.add(Coin.TWENTY_CENTS);
        coinsInserted.add(Coin.FIFTY_CENTS);
        coinsInserted.add(Coin.ONE_DOLLAR);
        coinsInserted.add(Coin.TWO_DOLLARS);
        vm.setCoinsInserted(coinsInserted);
        assertEquals(BigDecimal.valueOf(3.8), vm.getTotalAmountInserted());
    }

    //getChangeValue
    @Test
    public void computeForChangePositive() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.TWO_DOLLARS);
        coinsInserted.add(Coin.TWO_DOLLARS);
        vm.setCoinsInserted(coinsInserted);
        BigDecimal change = vm.getChangeValue(new Product("Hazelnut", BigDecimal.valueOf(3.10)));
        assertEquals(1, change.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void computeForChangeNegative() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.ONE_DOLLAR);
        vm.setCoinsInserted(coinsInserted);
        BigDecimal change = vm.getChangeValue(new Product("Hazelnut", BigDecimal.valueOf(3.10)));
        assertEquals(-1, change.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void computeForChangeZero() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.TEN_CENTS);
        coinsInserted.add(Coin.ONE_DOLLAR);
        coinsInserted.add(Coin.TWO_DOLLARS);
        vm.setCoinsInserted(coinsInserted);
        BigDecimal change = vm.getChangeValue(new Product("Hazelnut", BigDecimal.valueOf(3.10)));
        assertEquals(0, change.compareTo(BigDecimal.ZERO));
    }

    //test buyItem in sub class
    //cancelTransaction
    @Test
    public void cancelTransaction() {
        List<Coin> coinsInserted = new ArrayList<>();
        coinsInserted.add(Coin.TEN_CENTS);
        coinsInserted.add(Coin.ONE_DOLLAR);
        coinsInserted.add(Coin.TWO_DOLLARS);
        coinsInserted.add(Coin.TWO_DOLLARS);
        vm.setCoinsInserted(coinsInserted);

        vm.getChangeAvailable().put(Coin.TEN_CENTS, 1);
        vm.getChangeAvailable().put(Coin.ONE_DOLLAR, 3);
        vm.getChangeAvailable().put(Coin.TWO_DOLLARS, 3);

        vm.cancelTransaction();

        assertTrue(vm.getCoinsInserted().isEmpty());
        assertTrue(vm.getChangeAvailable().get(Coin.TEN_CENTS) == 0);
        assertTrue(vm.getChangeAvailable().get(Coin.ONE_DOLLAR) == 2);
        assertTrue(vm.getChangeAvailable().get(Coin.TWO_DOLLARS) == 1);
    }

    @Test
    public void retrieveAffordableItems() {
        vm.getProductList().put("1004", new Product("Caramel", BigDecimal.valueOf(2.50), 3));
        vm.getProductList().put("1005", new Product("Hazelnut", BigDecimal.valueOf(3.10), 2));
        vm.getProductList().put("1006", new Product("Organic Raw", BigDecimal.valueOf(2.00), 1));
        vm.getCoinsInserted().add(Coin.TWENTY_CENTS);
        vm.getCoinsInserted().add(Coin.FIFTY_CENTS);
        vm.getCoinsInserted().add(Coin.FIFTY_CENTS);
        vm.getCoinsInserted().add(Coin.FIFTY_CENTS);
        vm.getCoinsInserted().add(Coin.ONE_DOLLAR);
        HashMap<String, Product> affordProd = vm.getAffordProducts();
        assertTrue(affordProd.size() == 2);
        assertNotNull(affordProd.get("1004"));
        assertNotNull(affordProd.get("1006"));
    }

    @Test
    public void testDeductItemForPurchase() {
        vm.getProductList().put("1004", new Product("Caramel", BigDecimal.valueOf(2.50), 3));
        vm.deductItem("1004");
        vm.deductItem("1004");
        assertEquals(1, vm.getProductList().get("1004").getStock());
    }

    @Test
    public void testDeductItemForPurchaseToZero() {
        vm.getProductList().put("1004", new Product("Caramel", BigDecimal.valueOf(2.50), 1));
        vm.deductItem("1004");
        assertNull(vm.getProductList().get("1004"));
    }

    //deductChangeList subclass
    @Test
    public void coinIsNotExistingInChangeList() {
        assertFalse(vm.coinAvailableFromChange(Coin.TWENTY_CENTS, BigDecimal.valueOf(0)));
    }

    @Test
    public void balanceIsLessThanCoinValue() {
        assertFalse(vm.coinAvailableFromChange(Coin.TWENTY_CENTS, BigDecimal.valueOf(.10)));
    }

    @Test
    public void coinIsAvailableForChangeExactVal(){
        vm.getChangeAvailable().put(Coin.TWENTY_CENTS, 20);
        assertTrue(vm.coinAvailableFromChange(Coin.TWENTY_CENTS, BigDecimal.valueOf(.20)));
    }

    @Test
    public void coinIsAvailableForChange(){
        vm.getChangeAvailable().put(Coin.TWENTY_CENTS, 20);
        assertTrue(vm.coinAvailableFromChange(Coin.TWENTY_CENTS, BigDecimal.valueOf(1)));
    }

    @Test
    public void clearCoinsInsertedAfterPuchase(){
        Product caramel = new Product("Caramel", BigDecimal.valueOf(2.50));
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        vm.getCoinsInserted().add(Coin.TWO_DOLLARS);
        BigDecimal change = vm.getChangeValueClearCoinsInserted(caramel);
        assertTrue(vm.getCoinsInserted().isEmpty());
        assertEquals(BigDecimal.valueOf(1.50), change);
    }

    public VendingMachine createVending() {
        return new VendingMachine() {
            @Override
            public void insertCoin(String input) {}

            @Override
            public boolean hasAvailableChange(Product product) {
                return false;
            }

            @Override
            public List<Coin> getChange(Product product) {
                return null;
            }
        };

    }
}
