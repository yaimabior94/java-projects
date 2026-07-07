package com.smartinventory.oop;

public class FurnitureProduct extends Product implements Taxable {
    private String material;

    public FurnitureProduct(String name, double price, String material) {
        super(name, price);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String getCategory() {
        return "Furniture";
    }

    @Override
    public double calculateFinalPrice() {
        return getPrice() + calculateTax(getPrice());
    }

    @Override
    public double calculateTax(double price) {
        return price * 0.10;
    }
}
