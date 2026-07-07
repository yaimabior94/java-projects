package com.smartinventory.repository;

import com.smartinventory.dto.MonthlyTotalDto;
import com.smartinventory.dto.SalesReportDto;
import com.smartinventory.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findByInvoiceNumber(String invoiceNumber);

    List<Sale> findByCreatedById(Long userId);

    List<Sale> findByPaymentStatus(String paymentStatus);

    List<Sale> findByCustomerNameContainingIgnoreCase(String customerName);

    @Query("select new com.smartinventory.dto.SalesReportDto(" +
            "s.id, s.invoiceNumber, s.customerName, s.saleDate, s.totalAmount, s.discount, s.tax, " +
            "s.paymentStatus, u.fullName) " +
            "from Sale s " +
            "left join s.createdBy u " +
            "order by s.saleDate desc, s.id desc")
    List<SalesReportDto> findSalesReport();

    @Query("select sum(s.totalAmount) from Sale s")
    BigDecimal getTotalSalesAmount();

    @Query("select new com.smartinventory.dto.MonthlyTotalDto(" +
            "year(s.saleDate), month(s.saleDate), count(s), sum(s.totalAmount)) " +
            "from Sale s " +
            "group by year(s.saleDate), month(s.saleDate) " +
            "order by year(s.saleDate), month(s.saleDate)")
    List<MonthlyTotalDto> findMonthlySales();
}
