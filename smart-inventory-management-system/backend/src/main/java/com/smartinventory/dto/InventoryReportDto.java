package com.smartinventory.dto;

import java.math.BigDecimal;

public class InventoryReportDto {

    private Long productId;
    private String sku;
    private String productName;
    private String categoryName;
    private String supplierName;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private BigDecimal unitPrice;
    private Boolean active;

    public InventoryReportDto() {
    }

    public InventoryReportDto(Long productId, String sku, String productName, String categoryName,
                              String supplierName, Integer stockQuantity, Integer reorderLevel,
                              BigDecimal unitPrice, Boolean active) {
        this.productId = productId;
        this.sku = sku;
        this.productName = productName;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.unitPrice = unitPrice;
        this.active = active;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getStockValue() {
        BigDecimal price = unitPrice != null ? unitPrice : BigDecimal.ZERO;
        int quantity = stockQuantity != null ? stockQuantity : 0;
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
