package com.smartinventory.service;

import com.smartinventory.dto.DashboardSummaryDto;
import com.smartinventory.dto.LowStockProductDto;
import com.smartinventory.dto.MonthlyTotalDto;
import com.smartinventory.repository.CategoryRepository;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.PurchaseRepository;
import com.smartinventory.repository.SaleRepository;
import com.smartinventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;

    public DashboardService(ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            SupplierRepository supplierRepository,
                            SaleRepository saleRepository,
                            PurchaseRepository purchaseRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        return new DashboardSummaryDto(
                productRepository.count(),
                categoryRepository.count(),
                supplierRepository.count(),
                defaultAmount(saleRepository.getTotalSalesAmount()),
                defaultAmount(purchaseRepository.getTotalPurchasesAmount()),
                productRepository.findLowStockProducts(),
                saleRepository.findMonthlySales(),
                purchaseRepository.findMonthlyPurchases()
        );
    }

    @Transactional(readOnly = true)
    public List<LowStockProductDto> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    @Transactional(readOnly = true)
    public List<MonthlyTotalDto> getMonthlySales() {
        return saleRepository.findMonthlySales();
    }

    @Transactional(readOnly = true)
    public List<MonthlyTotalDto> getMonthlyPurchases() {
        return purchaseRepository.findMonthlyPurchases();
    }

    private BigDecimal defaultAmount(BigDecimal amount) {
        return amount != null ? amount : BigDecimal.ZERO;
    }
}
