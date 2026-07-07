package com.smartinventory.repository;

import com.smartinventory.entity.Category;
import com.smartinventory.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Repository tests for Product entity using Spring Data JPA
 *
 * @DataJpaTest - Disables full auto-configuration and applies only JPA configuration
 * Uses H2 in-memory database by default for testing
 * Each test is wrapped in a transaction and rolled back after execution
 */
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Product Repository Tests")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Create test category
        testCategory = new Category("Electronics", "Electronic products");
        entityManager.persistAndFlush(testCategory);
    }

    @Test
    @DisplayName("Should save product and retrieve by id")
    void testSaveAndFindById() {
        // Given - Create a product
        Product product = new Product();
        product.setSku("PROD-001");
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setUnitPrice(new BigDecimal("99.99"));
        product.setStockQuantity(100);
        product.setReorderLevel(20);
        product.setCategory(testCategory);

        // When - Save the product
        Product savedProduct = productRepository.save(product);

        // Then - Verify it's saved and can be retrieved
        Optional<Product> retrievedProduct = productRepository.findById(savedProduct.getId());
        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get().getSku()).isEqualTo("PROD-001");
        assertThat(retrievedProduct.get().getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should find product by SKU")
    void testFindBySku() {
        // Given - Product with unique SKU
        Product product = createProduct("SKU-12345", "Product A", testCategory);
        productRepository.save(product);

        // When - Find by SKU
        Optional<Product> found = productRepository.findBySku("SKU-12345");

        // Then - Should find the product
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Product A");
    }

    @Test
    @DisplayName("Should return empty when SKU not found")
    void testFindBySku_NotFound() {
        // When - Try to find non-existent SKU
        Optional<Product> found = productRepository.findBySku("NON-EXISTENT-SKU");

        // Then - Should return empty
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should search products by name (case-insensitive)")
    void testFindByNameContainingIgnoreCase() {
        // Given - Multiple products with similar names
        productRepository.save(createProduct("SKU-001", "Laptop Computer", testCategory));
        productRepository.save(createProduct("SKU-002", "Desktop Computer", testCategory));
        productRepository.save(createProduct("SKU-003", "Mouse", testCategory));

        // When - Search for "computer" (lowercase)
        List<Product> results = productRepository.findByNameContainingIgnoreCase("computer");

        // Then - Should find both computer products (case-insensitive)
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name")
                .contains("Laptop Computer", "Desktop Computer");
    }

    @Test
    @DisplayName("Should find products by category")
    void testFindByCategoryId() {
        // Given - Create another category and products
        Category category2 = new Category("Furniture", "Furniture items");
        entityManager.persistAndFlush(category2);

        productRepository.save(createProduct("SKU-001", "Laptop", testCategory));
        productRepository.save(createProduct("SKU-002", "Desk", category2));
        productRepository.save(createProduct("SKU-003", "Monitor", testCategory));

        // When - Find products by first category
        List<Product> results = productRepository.findByCategoryId(testCategory.getId());

        // Then - Should return only products in that category
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name")
                .contains("Laptop", "Monitor");
    }

    @Test
    @DisplayName("Should find active products only")
    void testFindByIsActiveTrue() {
        // Given - Mix of active and inactive products
        Product activeProduct1 = createProduct("SKU-001", "Active Product 1", testCategory);
        activeProduct1.setIsActive(true);
        productRepository.save(activeProduct1);

        Product inactiveProduct = createProduct("SKU-002", "Inactive Product", testCategory);
        inactiveProduct.setIsActive(false);
        productRepository.save(inactiveProduct);

        Product activeProduct2 = createProduct("SKU-003", "Active Product 2", testCategory);
        activeProduct2.setIsActive(true);
        productRepository.save(activeProduct2);

        // When - Find only active products
        List<Product> activeProducts = productRepository.findByIsActiveTrue();

        // Then - Should return only active products
        assertThat(activeProducts).hasSize(2);
        assertThat(activeProducts).allMatch(p -> p.getIsActive().equals(true));
    }

    @Test
    @DisplayName("Should find low stock products")
    void testFindByStockQuantityLessThanEqual() {
        // Given - Products with different stock levels
        Product lowStockProduct1 = createProduct("SKU-001", "Product A", testCategory);
        lowStockProduct1.setStockQuantity(5);
        productRepository.save(lowStockProduct1);

        Product lowStockProduct2 = createProduct("SKU-002", "Product B", testCategory);
        lowStockProduct2.setStockQuantity(10);
        productRepository.save(lowStockProduct2);

        Product normalStockProduct = createProduct("SKU-003", "Product C", testCategory);
        normalStockProduct.setStockQuantity(100);
        productRepository.save(normalStockProduct);

        // When - Find products with stock <= 10
        List<Product> lowStockProducts = productRepository.findByStockQuantityLessThanEqual(10);

        // Then - Should return products with stock <= 10
        assertThat(lowStockProducts).hasSize(2);
        assertThat(lowStockProducts).extracting("name")
                .contains("Product A", "Product B");
    }

    @Test
    @DisplayName("Should update existing product")
    void testUpdateProduct() {
        // Given - Existing product
        Product product = productRepository.save(createProduct("SKU-001", "Original Name", testCategory));
        Long productId = product.getId();

        // When - Update product
        product.setName("Updated Name");
        product.setStockQuantity(50);
        productRepository.save(product);

        // Then - Changes should be persisted
        Product updated = productRepository.findById(productId).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getStockQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should delete product")
    void testDeleteProduct() {
        // Given - Saved product
        Product product = productRepository.save(createProduct("SKU-001", "To Delete", testCategory));
        Long productId = product.getId();

        // When - Delete the product
        productRepository.deleteById(productId);

        // Then - Product should not exist
        assertThat(productRepository.findById(productId)).isEmpty();
    }

    @Test
    @DisplayName("Should count total products")
    void testCountAllProducts() {
        // Given - Multiple products
        productRepository.save(createProduct("SKU-001", "Product 1", testCategory));
        productRepository.save(createProduct("SKU-002", "Product 2", testCategory));
        productRepository.save(createProduct("SKU-003", "Product 3", testCategory));

        // When - Count all
        long count = productRepository.count();

        // Then - Should return correct count
        assertThat(count).isEqualTo(3);
    }

    // Helper method to create test products
    private Product createProduct(String sku, String name, Category category) {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setDescription("Description for " + name);
        product.setUnitPrice(new BigDecimal("99.99"));
        product.setStockQuantity(100);
        product.setReorderLevel(20);
        product.setIsActive(true);
        product.setCategory(category);
        return product;
    }
}
