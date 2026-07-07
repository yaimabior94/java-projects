package com.smartinventory.repository;

import com.smartinventory.dto.MonthlyTotalDto;
import com.smartinventory.dto.PurchaseReportDto;
import com.smartinventory.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findByPurchaseNumber(String purchaseNumber);

    List<Purchase> findBySupplierId(Long supplierId);

    List<Purchase> findByCreatedById(Long userId);

    List<Purchase> findByPaymentStatus(String paymentStatus);

    @Query("select new com.smartinventory.dto.PurchaseReportDto(" +
            "p.id, p.purchaseNumber, p.purchaseDate, s.name, p.totalAmount, p.paymentStatus, u.fullName) " +
            "from Purchase p " +
            "left join p.supplier s " +
            "left join p.createdBy u " +
            "order by p.purchaseDate desc, p.id desc")
    List<PurchaseReportDto> findPurchaseReport();

    @Query("select sum(p.totalAmount) from Purchase p")
    BigDecimal getTotalPurchasesAmount();

    @Query("select new com.smartinventory.dto.MonthlyTotalDto(" +
            "year(p.purchaseDate), month(p.purchaseDate), count(p), sum(p.totalAmount)) " +
            "from Purchase p " +
            "group by year(p.purchaseDate), month(p.purchaseDate) " +
            "order by year(p.purchaseDate), month(p.purchaseDate)")
    List<MonthlyTotalDto> findMonthlyPurchases();
}
