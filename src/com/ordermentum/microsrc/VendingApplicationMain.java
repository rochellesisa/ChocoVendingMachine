package com.ordermentum.microsrc;

import com.ordermentum.microsrc.comp.Coin;
import com.ordermentum.microsrc.comp.Product;
import com.ordermentum.microsrc.exception.InvalidInputException;
import com.ordermentum.microsrc.impl.ChocoVendingMachine;
import com.ordermentum.microsrc.impl.VendingMachine;

import java.math.BigDecimal;
import java.util.Scanner;

public class VendingApplicationMain {

    public static void main(String[] args) {
        VendingMachine vm = new ChocoVendingMachine();
        populateMachine(vm);
        Scanner scan = new Scanner(System.in);
        String flag = "Y";
        while(flag.equals("Y")){
            start(vm);
            System.out.print("Start another transaction? Y/N: ");
            flag = scan.nextLine();
        }
    }

    private static void start(VendingMachine vm){
        System.out.println("Welcome!");
        displayItems(vm);
        System.out.println("Please insert coins to proceed with the transaction");
        System.out.println("Valid denominations are as follows: 10c, 20c, 50c, $1, $2");
        System.out.println("Enter '#' to cancel the transaction");

        Product selectedProduct = null;
        Scanner sc = new Scanner(System.in);

        while (selectedProduct == null) {
            System.out.print("\nInput: ");
            String input = sc.nextLine();
            selectedProduct = processInput(vm, input);
            if (selectedProduct == null)
                displayAffordProducts(vm);
        }

        System.out.println();
        System.out.println("Bought " + selectedProduct.getName() + " for $" + selectedProduct.getCost());
        System.out.println("Change is $" + vm.getChangeValueClearCoinsInserted(selectedProduct));
    }

    private static void populateMachine(VendingMachine vm) {
        vm.restockCoins(Coin.TEN_CENTS, 5);
        vm.restockCoins(Coin.TWENTY_CENTS, 5);
        vm.restockCoins(Coin.FIFTY_CENTS, 2);
        vm.restockCoins(Coin.ONE_DOLLAR, 5);
        vm.restockCoins(Coin.TWO_DOLLARS, 5);

        vm.restockItem("1001", new Product("Caramel", BigDecimal.valueOf(2.50).setScale(2)), 3);
        vm.restockItem("1002", new Product("Hazelnut", BigDecimal.valueOf(3.10).setScale(2)), 3);
        vm.restockItem("1003", new Product("Organic Raw", BigDecimal.valueOf(2.00).setScale(2)), 3);
    }

    private static void displayItems(VendingMachine vm) {
        System.out.println("\nAvailable products:");
        for (String code : vm.getProductList().keySet()) {
            Product p = vm.getProductByCode(code);
            System.out.println("\t" + code + " - " + p.getName() + " - $" + p.getCost());
        }
        System.out.println();
    }

    private static Product processInput(VendingMachine vm, String input) {
        Product selectedProduct = null;

        if (input.equals("#")) {
            vm.cancelTransaction();
            System.out.println("Transaction cancelled");
            System.exit(0);
        }

        try {
            if (input.equals("P") && !vm.getAffordProducts().isEmpty()) {
                try {
                    selectedProduct = buyProduct(vm);
//                    if (selectedProduct == null) {
//                        processInput(vm, "P");
//                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please purchase another product or cancel the transaction");
                    return null;
                }
            } else {
                vm.insertCoin(input);
                System.out.println("Current balance: $" + vm.getTotalAmountInserted().setScale(2));
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
        return selectedProduct;
    }

    private static void displayAffordProducts(VendingMachine vm) {
        StringBuilder sb = new StringBuilder("\nProducts available for purchase:");
        for (String code : vm.getAffordProducts().keySet()) {
            Product p = vm.getProductByCode(code);
            if (vm.getTotalAmountInserted().compareTo(p.getCost()) >= 0) {
                sb.append("\n\t" + code + " - " + p.getName() + " - $" + p.getCost());
            }
        }

        if (!vm.getAffordProducts().isEmpty()) {
            System.out.println(sb.toString());
            System.out.println("Input 'P' to make a purchase");
        }
    }

    private static Product buyProduct(VendingMachine vm) {
        System.out.println("\nTo make a purchase, input the product code");
        System.out.println("To insert money, input '*'");
        System.out.println("To cancel the transaction, input '#'");
        System.out.print("Input: ");
        Scanner sc = new Scanner(System.in);
        String sel = sc.nextLine();
        Product selProd = null;

        switch (sel) {
            case "*":
                break;
            case "#":
                vm.cancelTransaction();
                System.out.println("Transaction cancelled");
                System.exit(0);
                break;
            default:
                selProd = vm.getProductByCode(sel);
                vm.buyItem(sel);
                break;
        }
        System.out.println();
        return selProd;
    }
}
