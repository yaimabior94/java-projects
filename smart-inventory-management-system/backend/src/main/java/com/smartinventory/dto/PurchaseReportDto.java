package com.smartinventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseReportDto {

    private Long purchaseId;
    private String purchaseNumber;
    private LocalDateTime purchaseDate;
    private String supplierName;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String createdBy;

    public PurchaseReportDto() {
    }

    public PurchaseReportDto(Long purchaseId, String purchaseNumber, LocalDateTime purchaseDate,
                             String supplierName, BigDecimal totalAmount, String paymentStatus,
                             String createdBy) {
        this.purchaseId = purchaseId;
        this.purchaseNumber = purchaseNumber;
        this.purchaseDate = purchaseDate;
        this.supplierName = supplierName;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.createdBy = createdBy;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseNumber() {
        return purchaseNumber;
    }

    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
