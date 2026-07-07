package com.smartinventory.oop;

public class ElectronicProduct extends Product implements Taxable, Discountable {
    private double warrantyYears;

    public ElectronicProduct(String name, double price, double warrantyYears) {
        super(name, price);
        this.warrantyYears = warrantyYears;
    }

    public double getWarrantyYears() {
        return warrantyYears;
    }

    public void setWarrantyYears(double warrantyYears) {
        this.warrantyYears = warrantyYears;
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }

    @Override
    public double calculateFinalPrice() {
        return applyDiscount(getPrice()) + calculateTax(applyDiscount(getPrice()));
    }

    @Override
    public double applyDiscount(double price) {
        return price * 0.90;
    }

    @Override
    public double calculateTax(double price) {
        return price * 0.12;
    }
}
