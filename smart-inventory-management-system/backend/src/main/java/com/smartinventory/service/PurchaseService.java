package com.smartinventory.service;

import com.smartinventory.entity.Purchase;
import com.smartinventory.entity.PurchaseItem;
import com.smartinventory.entity.Product;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
    }

    /**
     * Business Rule Case 1: Purchase (Add Stock)
     * When purchasing products from suppliers, add the quantity to existing stock.
     * Example: Current Stock = 50, Buy Quantity = 20, Result Stock = 70
     */
    @Transactional
    public Purchase createPurchase(Purchase purchase) {
        if (purchase.getPurchaseItems() == null || purchase.getPurchaseItems().isEmpty()) {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }

        for (PurchaseItem item : purchase.getPurchaseItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Purchase quantity must be greater than zero");
            }

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + item.getProduct().getId()));

            Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
            Integer newStock = currentStock + item.getQuantity();

            product.setStockQuantity(newStock);
            productRepository.save(product);

            // Set the parent relationship
            item.setPurchase(purchase);
        }

        return purchaseRepository.save(purchase);
    }

    /**
     * Add stock to a product directly (for direct purchase operations).
     * Stock calculation: newStock = currentStock + quantityPurchased
     */
    @Transactional
    public void addStockFromPurchase(Long productId, Integer quantityPurchased) {
        if (quantityPurchased == null || quantityPurchased <= 0) {
            throw new IllegalArgumentException("Purchase quantity must be greater than zero");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        Integer newStock = currentStock + quantityPurchased;

        product.setStockQuantity(newStock);
        productRepository.save(product);
    }

    /**
     * Updates purchase header fields only (purchase number, date, amount, status, supplier).
     * NOTE: PurchaseItems cannot be modified through this endpoint. To change quantities,
     * delete this purchase and create a new one. This preserves stock integrity.
     */
    @Transactional
    public Purchase updatePurchase(Long id, Purchase updatedPurchase) {
        Purchase existing = getPurchaseById(id);
        existing.setPurchaseNumber(updatedPurchase.getPurchaseNumber());
        existing.setPurchaseDate(updatedPurchase.getPurchaseDate());
        existing.setTotalAmount(updatedPurchase.getTotalAmount());
        existing.setPaymentStatus(updatedPurchase.getPaymentStatus());
        existing.setSupplier(updatedPurchase.getSupplier());
        existing.setCreatedBy(updatedPurchase.getCreatedBy());
        return purchaseRepository.save(existing);
    }

    /**
     * FIX BUG-04: Before deleting a purchase, deduct the stock that was added
     * when this purchase was originally created. Without this fix, stock added
     * at creation time is never removed, causing phantom inventory.
     */
    @Transactional
    public void deletePurchase(Long id) {
        Purchase purchase = getPurchaseById(id);

        // Deduct stock for every item in this purchase before deletion
        for (PurchaseItem item : purchase.getPurchaseItems()) {
            if (item.getProduct() == null || item.getQuantity() == null) continue;
            productRepository.findById(item.getProduct().getId()).ifPresent(product -> {
                Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
                // Clamp to 0 to avoid negative stock from external stock adjustments
                Integer newStock = Math.max(0, currentStock - item.getQuantity());
                product.setStockQuantity(newStock);
                productRepository.save(product);
            });
        }

        purchaseRepository.delete(purchase);
    }
}
