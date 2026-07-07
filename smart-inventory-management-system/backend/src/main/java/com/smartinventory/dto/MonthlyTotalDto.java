package com.smartinventory.dto;

import java.math.BigDecimal;
import java.time.Month;

public class MonthlyTotalDto {

    private Integer year;
    private Integer month;
    private String monthName;
    private Long totalRecords;
    private BigDecimal totalAmount;

    public MonthlyTotalDto() {
    }

    public MonthlyTotalDto(Integer year, Integer month, Long totalRecords, BigDecimal totalAmount) {
        this.year = year;
        this.month = month;
        this.monthName = month != null ? Month.of(month).name() : null;
        this.totalRecords = totalRecords;
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
        this.monthName = month != null ? Month.of(month).name() : null;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
