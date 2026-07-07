package com.smartinventory.oop;

import java.util.ArrayList;
import java.util.List;

public class ProductDemo {
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();

        products.add(new ElectronicProduct("Laptop", 1000, 2));
        products.add(new FoodProduct("Bread", 5, "2026-07-01"));
        products.add(new FurnitureProduct("Chair", 200, "Wood"));

        for (Product product : products) {
            System.out.println("Product: " + product.getName());
            System.out.println("Category: " + product.getCategory());
            System.out.println("Final Price: " + product.calculateFinalPrice());
            System.out.println();
        }
    }
}
