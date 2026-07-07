package com.smartinventory.service;

import com.smartinventory.entity.Inventory;
import com.smartinventory.entity.Product;
import com.smartinventory.exception.InsufficientStockException;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.repository.InventoryRepository;
import com.smartinventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));
    }

    @Transactional(readOnly = true)
    public List<Inventory> getLowStockInventory(Integer threshold) {
        return inventoryRepository.findByQuantityOnHandLessThanEqual(threshold);
    }

    @Transactional
    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory updateInventory(Long id, Inventory updatedInventory) {
        Inventory existing = getInventoryById(id);
        existing.setQuantityOnHand(updatedInventory.getQuantityOnHand());
        existing.setQuantityReserved(updatedInventory.getQuantityReserved());
        existing.setLastStockCheck(updatedInventory.getLastStockCheck());
        return inventoryRepository.save(existing);
    }

    @Transactional
    public void reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // FIX BUG-02: guard against null stockQuantity to prevent NullPointerException
        Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        if (currentStock < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(currentStock - quantity);
        productRepository.save(product);
    }

    @Transactional
    public void addStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // FIX BUG-02: guard against null stockQuantity to prevent NullPointerException
        Integer currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        product.setStockQuantity(currentStock + quantity);
        productRepository.save(product);
    }
}
