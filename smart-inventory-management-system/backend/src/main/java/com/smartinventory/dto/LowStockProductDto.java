package com.smartinventory.dto;

public class LowStockProductDto {

    private Long id;
    private String sku;
    private String name;
    private String categoryName;
    private String supplierName;
    private Integer stockQuantity;
    private Integer reorderLevel;

    public LowStockProductDto() {
    }

    public LowStockProductDto(Long id, String sku, String name, String categoryName, String supplierName,
                              Integer stockQuantity, Integer reorderLevel) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.categoryName = categoryName;
        this.supplierName = supplierName;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
