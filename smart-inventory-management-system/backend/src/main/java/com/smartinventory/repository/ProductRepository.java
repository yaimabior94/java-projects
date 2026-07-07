package com.smartinventory.repository;

import com.smartinventory.dto.LowStockProductDto;
import com.smartinventory.dto.InventoryReportDto;
import com.smartinventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findBySupplierId(Long supplierId);

    List<Product> findByIsActiveTrue();

    List<Product> findByStockQuantityLessThanEqual(Integer quantity);

    List<Product> findByStockQuantityLessThan(Integer quantity);

    @Query("select new com.smartinventory.dto.InventoryReportDto(" +
            "p.id, p.sku, p.name, c.name, s.name, p.stockQuantity, p.reorderLevel, p.unitPrice, p.isActive) " +
            "from Product p " +
            "left join p.category c " +
            "left join p.supplier s " +
            "order by p.name asc")
    List<InventoryReportDto> findInventoryReport();

    @Query("select new com.smartinventory.dto.LowStockProductDto(" +
            "p.id, p.sku, p.name, c.name, s.name, p.stockQuantity, p.reorderLevel) " +
            "from Product p " +
            "left join p.category c " +
            "left join p.supplier s " +
            "where p.stockQuantity <= p.reorderLevel " +
            "order by p.stockQuantity asc, p.name asc")
    List<LowStockProductDto> findLowStockProducts();
}
