package com.smartinventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SalesReportDto {

    private Long saleId;
    private String invoiceNumber;
    private String customerName;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal tax;
    private String paymentStatus;
    private String createdBy;

    public SalesReportDto() {
    }

    public SalesReportDto(Long saleId, String invoiceNumber, String customerName, LocalDateTime saleDate,
                          BigDecimal totalAmount, BigDecimal discount, BigDecimal tax,
                          String paymentStatus, String createdBy) {
        this.saleId = saleId;
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.tax = tax;
        this.paymentStatus = paymentStatus;
        this.createdBy = createdBy;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
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
