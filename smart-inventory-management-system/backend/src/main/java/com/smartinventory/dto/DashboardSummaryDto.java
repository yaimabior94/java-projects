package com.smartinventory.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummaryDto {

    private Long totalProducts;
    private Long totalCategories;
    private Long totalSuppliers;
    private BigDecimal totalSales;
    private BigDecimal totalPurchases;
    private List<LowStockProductDto> lowStockProducts;
    private List<MonthlyTotalDto> monthlySales;
    private List<MonthlyTotalDto> monthlyPurchases;

    public DashboardSummaryDto() {
    }

    public DashboardSummaryDto(Long totalProducts, Long totalCategories, Long totalSuppliers,
                               BigDecimal totalSales, BigDecimal totalPurchases,
                               List<LowStockProductDto> lowStockProducts,
                               List<MonthlyTotalDto> monthlySales,
                               List<MonthlyTotalDto> monthlyPurchases) {
        this.totalProducts = totalProducts;
        this.totalCategories = totalCategories;
        this.totalSuppliers = totalSuppliers;
        this.totalSales = totalSales;
        this.totalPurchases = totalPurchases;
        this.lowStockProducts = lowStockProducts;
        this.monthlySales = monthlySales;
        this.monthlyPurchases = monthlyPurchases;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(Long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public Long getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(Long totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(BigDecimal totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public List<LowStockProductDto> getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(List<LowStockProductDto> lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public List<MonthlyTotalDto> getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(List<MonthlyTotalDto> monthlySales) {
        this.monthlySales = monthlySales;
    }

    public List<MonthlyTotalDto> getMonthlyPurchases() {
        return monthlyPurchases;
    }

    public void setMonthlyPurchases(List<MonthlyTotalDto> monthlyPurchases) {
        this.monthlyPurchases = monthlyPurchases;
    }
}
