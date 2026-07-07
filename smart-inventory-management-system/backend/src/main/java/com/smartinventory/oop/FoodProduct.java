package com.smartinventory.oop;

public class FoodProduct extends Product implements Discountable {
    private String expiryDate;

    public FoodProduct(String name, double price, String expiryDate) {
        super(name, price);
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getCategory() {
        return "Food";
    }

    @Override
    public double calculateFinalPrice() {
        return applyDiscount(getPrice());
    }

    @Override
    public double applyDiscount(double price) {
        return price * 0.95;
    }
}
