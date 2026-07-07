package com.smartinventory.service;

import com.smartinventory.entity.Product;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByStockQuantityLessThanEqual(threshold);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);
        existing.setSku(updatedProduct.getSku());
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setCategory(updatedProduct.getCategory());
        existing.setSupplier(updatedProduct.getSupplier());
        existing.setUnitPrice(updatedProduct.getUnitPrice());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setReorderLevel(updatedProduct.getReorderLevel());
        existing.setIsActive(updatedProduct.getIsActive());
        return productRepository.save(existing);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
