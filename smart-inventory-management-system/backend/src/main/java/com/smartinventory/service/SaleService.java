package com.smartinventory.service;

import com.smartinventory.entity.Product;
import com.smartinventory.entity.Sale;
import com.smartinventory.entity.SaleItem;
import com.smartinventory.exception.InsufficientStockException;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
    }

    /**
     * Business Rule Case 2: Sale (Reduce Stock with Validation)
     * When selling products to customers, reduce the stock quantity.
     * Throws InsufficientStockException if stock is insufficient.
     * Example: Current Stock = 70, Sell Quantity = 15, Result Stock = 55
     * If Sell Quantity > Current Stock, exception is thrown.
     *
     * FIX BUG-01: Products are now fetched once and cached in a Map to eliminate
     * the double-fetch race condition. The same product objects are reused in the
     * stock-reduction pass, preventing stale reads between validation and update.
     */
    @Transactional
    public Sale createSale(Sale sale) {
        if (sale.getSaleItems() == null || sale.getSaleItems().isEmpty()) {
            throw new IllegalArgumentException("Sale must contain at least one item");
        }

        // Fetch and cache all products in a single pass to avoid double DB round-trips
        // and reduce the window for race conditions.
        Map<Long, Product> productCache = new HashMap<>();
        for (SaleItem item : sale.getSaleItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Sale quantity must be greater than zero");
            }
            Long productId = item.getProduct().getId();
            productCache.computeIfAbsent(productId, pid ->
                    productRepository.findById(pid)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + pid))
            );
        }

        // Validate that all products have sufficient stock using the cached copies
        for (SaleItem item : sale.getSaleItems()) {
            Product product = productCache.get(item.getProduct().getId());
            Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
            if (currentStock < item.getQuantity()) {
                throw new InsufficientStockException(
                    "Insufficient stock for product: " + product.getName() +
                    ". Available: " + currentStock + ", Requested: " + item.getQuantity()
                );
            }
        }

        // Reduce stock for all items using the cached product objects (no second DB fetch).
        // FIX BUG-A: item.setSale(sale) must be called so JPA cascade can persist each SaleItem
        // with the correct FK reference. The sale entity holds the owning side of the collection.
        for (SaleItem item : sale.getSaleItems()) {
            Product product = productCache.get(item.getProduct().getId());
            Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
            product.setStockQuantity(currentStock - item.getQuantity());
            productRepository.save(product);
            item.setSale(sale); // ensure back-reference is set before cascade save
        }

        return saleRepository.save(sale);
    }

    /**
     * Reduce stock for a product directly (for direct sales operations).
     * Throws InsufficientStockException if stock is insufficient.
     * Stock calculation: newStock = currentStock - quantitySold
     */
    @Transactional
    public void reduceStockFromSale(Long productId, Integer quantitySold) {
        if (quantitySold == null || quantitySold <= 0) {
            throw new IllegalArgumentException("Sale quantity must be greater than zero");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;

        if (currentStock < quantitySold) {
            throw new InsufficientStockException(
                "Insufficient stock for product: " + product.getName() +
                ". Available: " + currentStock + ", Requested: " + quantitySold
            );
        }

        Integer newStock = currentStock - quantitySold;
        product.setStockQuantity(newStock);
        productRepository.save(product);
    }

    /**
     * Updates sale header fields only (invoice number, customer, dates, amounts, status).
     * NOTE: SaleItems cannot be modified through this endpoint. To change quantities,
     * delete this sale and create a new one. This preserves stock integrity.
     * FIX BUG-C: createdBy is never overwritten on update — doing so with a partial
     * User object (id only) would corrupt the relationship.
     */
    @Transactional
    public Sale updateSale(Long id, Sale updatedSale) {
        Sale existing = getSaleById(id);
        existing.setInvoiceNumber(updatedSale.getInvoiceNumber());
        existing.setCustomerName(updatedSale.getCustomerName());
        existing.setSaleDate(updatedSale.getSaleDate());
        existing.setTotalAmount(updatedSale.getTotalAmount());
        existing.setDiscount(updatedSale.getDiscount());
        existing.setTax(updatedSale.getTax());
        existing.setPaymentStatus(updatedSale.getPaymentStatus());
        // Do NOT update createdBy — it is set once at creation and must not be changed.
        return saleRepository.save(existing);
    }

    /**
     * FIX BUG-04: Before deleting a sale, restore the stock for each sale item.
     * Without this fix, stock deducted at creation time is permanently lost.
     */
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = getSaleById(id);

        // Restore stock for every item in this sale before deletion
        for (SaleItem item : sale.getSaleItems()) {
            if (item.getProduct() == null || item.getQuantity() == null) continue;
            productRepository.findById(item.getProduct().getId()).ifPresent(product -> {
                Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
                product.setStockQuantity(currentStock + item.getQuantity());
                productRepository.save(product);
            });
        }

        saleRepository.delete(sale);
    }
}
