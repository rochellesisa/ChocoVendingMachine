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
        System.out.println("Change is $" + vm.getChangeValue(selectedProduct));


    }

    private static void populateMachine(VendingMachine vm) {
        vm.restockItem(Coin.TEN_CENTS, 5);
        vm.restockItem(Coin.TWENTY_CENTS, 5);
        vm.restockItem(Coin.FIFTY_CENTS, 5);
        vm.restockItem(Coin.ONE_DOLLAR, 5);
        vm.restockItem(Coin.TWO_DOLLARS, 5);

        vm.restockItem(new Product("Caramel", "1001", new BigDecimal(2.50)), 5);
        vm.restockItem(new Product("Hazelnut", "1002", new BigDecimal(3.10)), 5);
        vm.restockItem(new Product("Organic Raw", "1003", new BigDecimal(2.00)), 5);
    }

    private static void displayItems(VendingMachine vm) {
        System.out.println("\nAvailable products:");
        for (Product p : vm.getProductList().keySet()) {
            if (vm.getProductList().get(p) > 0)
                System.out.println("\t" + p.getKey() + " - " + p.getName() + " - $" + p.getCost());
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
                    if (selectedProduct == null) {
                        System.out.println("Invalid product code/Insufficient payment");
                        processInput(vm, "P");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please purchase another product or cancel the transaction");
                    return null;
                }
            } else {
                vm.insertCoin(input);
                System.out.println("Current balance: " + vm.getTotalAmountInserted());
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
        return selectedProduct;
    }

    private static void displayAffordProducts(VendingMachine vm) {
        StringBuilder sb = new StringBuilder("\nProducts available for purchase:");
        for (Product p : vm.getAffordProducts()) {
            if (vm.getProductList().get(p) > 0 && vm.getTotalAmountInserted().compareTo(p.getCost()) >= 0) {
                sb.append("\n\t" + p.getKey() + " - " + p.getName() + " - $" + p.getCost());
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
            default:
                for (Product p : vm.getAffordProducts()) {
                    if (p.getKey().equals(sel)) {
                        selProd = p;
                        vm.buyItem(p);
                    }
                }
        }
        System.out.println();
        return selProd;
    }
}
