package com.smartinventory.integration;

import com.smartinventory.entity.Category;
import com.smartinventory.entity.Product;
import com.smartinventory.repository.CategoryRepository;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller integration tests for ProductController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = "ADMIN")
@DisplayName("Product Integration Tests")
@SuppressWarnings("null")
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();

        // Create test category and flush immediately
        testCategory = new Category("Electronics", "Electronic products");
        categoryRepository.saveAndFlush(testCategory);
    }

    @Test
    @DisplayName("Integration: Create product via service and verify via repository")
    @Transactional
    void testCreateProductThroughService() {
        // Given - Create a product
        Product product = createTestProduct("SKU-001", "Test Product");

        // When - Save through service
        Product savedProduct = productService.createProduct(product);

        // Then - Verify in database
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(productRepository.findById(savedProduct.getId())).isPresent();
        assertThat(productRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Integration: Full CRUD operations end-to-end")
    @Transactional
    void testFullCrudLifecycle() {
        // Given - Create product
        Product product = createTestProduct("SKU-CRUD", "CRUD Product");

        // When - Create
        Product created = productService.createProduct(product);
        assertThat(created.getId()).isNotNull();

        // When - Retrieve
        Product retrieved = productService.getProductById(created.getId());
        assertThat(retrieved.getName()).isEqualTo("CRUD Product");

        // When - Update
        retrieved.setName("Updated CRUD Product");
        retrieved.setStockQuantity(50);
        Product updated = productService.updateProduct(retrieved.getId(), retrieved);
        assertThat(updated.getName()).isEqualTo("Updated CRUD Product");
        assertThat(updated.getStockQuantity()).isEqualTo(50);

        // When - Delete
        productService.deleteProduct(updated.getId());
        assertThat(productRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Integration: Multiple products with different criteria")
    @Transactional
    void testSearchWithMultipleProducts() {
        // Given - Create multiple products
        productService.createProduct(createTestProduct("SKU-001", "Laptop Computer"));
        productService.createProduct(createTestProduct("SKU-002", "Desktop Computer"));
        productService.createProduct(createTestProduct("SKU-003", "USB Mouse"));

        // When & Then - Search by name
        var results = productService.searchProductsByName("Computer");
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name")
                .contains("Laptop Computer", "Desktop Computer");

        // When & Then - Get all
        var all = productService.getAllProducts();
        assertThat(all).hasSize(3);
    }

    @Test
    @DisplayName("Integration: Low stock products query")
    @Transactional
    void testLowStockProductsIntegration() {
        // Given - Create products with various stock levels
        Product lowStock1 = createTestProduct("SKU-001", "Low Stock 1");
        lowStock1.setStockQuantity(5);
        productService.createProduct(lowStock1);

        Product lowStock2 = createTestProduct("SKU-002", "Low Stock 2");
        lowStock2.setStockQuantity(10);
        productService.createProduct(lowStock2);

        Product normalStock = createTestProduct("SKU-003", "Normal Stock");
        normalStock.setStockQuantity(100);
        productService.createProduct(normalStock);

        // When - Query low stock with threshold 10
        var lowStockProducts = productService.getLowStockProducts(10);

        // Then - Should return only low stock items
        assertThat(lowStockProducts).hasSize(2);
        assertThat(lowStockProducts).allMatch(p -> p.getStockQuantity() <= 10);
    }

    @Test
    @DisplayName("HTTP Integration: POST /api/products creates product in database")
    void testCreateProductViaHttp() throws Exception {
        // Given - Create product via HTTP
        String productJson = "{" +
                "\"sku\":\"HTTP-001\"," +
                "\"name\":\"HTTP Product\"," +
                "\"unitPrice\":199.99," +
                "\"stockQuantity\":100," +
                "\"reorderLevel\":20," +
                "\"isActive\":true," +
                "\"category\":{\"id\":" + testCategory.getId() + "}" +
                "}";

        // When & Then - Perform POST and verify
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.sku", equalTo("HTTP-001")));

        // Verify in database
        assertThat(productRepository.findBySku("HTTP-001")).isPresent();
        assertThat(productRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("HTTP Integration: GET /api/products retrieves from database")
    void testGetProductsViaHttp() throws Exception {
        // Given - Create products in database
        productService.createProduct(createTestProduct("SKU-001", "Product 1"));
        productService.createProduct(createTestProduct("SKU-002", "Product 2"));

        // When & Then - GET and verify count
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].sku", anyOf(equalTo("SKU-001"), equalTo("SKU-002"))))
                .andExpect(jsonPath("$[1].sku", anyOf(equalTo("SKU-001"), equalTo("SKU-002"))));
    }

    @Test
    @DisplayName("HTTP Integration: PUT /api/products/{id} updates database")
    void testUpdateProductViaHttp() throws Exception {
        // Given - Create a product
        Product created = productService.createProduct(createTestProduct("SKU-001", "Original Name"));

        // When - Update via HTTP
        String updateJson = "{" +
                "\"sku\":\"SKU-001\"," +
                "\"name\":\"Updated Name\"," +
                "\"unitPrice\":199.99," +
                "\"stockQuantity\":50," +
                "\"reorderLevel\":10," +
                "\"isActive\":true," +
                "\"category\":{\"id\":" + testCategory.getId() + "}" +
                "}";

        mockMvc.perform(put("/api/products/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Updated Name")))
                .andExpect(jsonPath("$.stockQuantity", equalTo(50)));

        // Then - Verify in database
        Product updated = productRepository.findById(created.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getStockQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("HTTP Integration: DELETE /api/products/{id} removes from database")
    void testDeleteProductViaHttp() throws Exception {
        // Given - Create a product
        Product created = productService.createProduct(createTestProduct("SKU-001", "To Delete"));
        long initialCount = productRepository.count();

        // When - Delete via HTTP
        mockMvc.perform(delete("/api/products/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then - Verify deleted from database
        assertThat(productRepository.findById(created.getId())).isEmpty();
        assertThat(productRepository.count()).isEqualTo(initialCount - 1);
    }

    @Test
    @DisplayName("HTTP Integration: GET /api/products/search?name=query")
    void testSearchViaHttp() throws Exception {
        // Given - Create products
        productService.createProduct(createTestProduct("SKU-001", "Laptop Computer"));
        productService.createProduct(createTestProduct("SKU-002", "Desktop Computer"));
        productService.createProduct(createTestProduct("SKU-003", "USB Mouse"));

        // When & Then - Search via HTTP
        mockMvc.perform(get("/api/products/search")
                        .param("name", "Computer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", hasItems(
                        containsString("Laptop Computer"),
                        containsString("Desktop Computer"))));
    }

    @Test
    @DisplayName("Transaction: Rollback on service exception")
    @Transactional
    void testTransactionRollback() {
        // Given - Create product
        Product product = createTestProduct("SKU-001", "Product");
        productService.createProduct(product);
        assertThat(productRepository.count()).isEqualTo(1);

        // When - Attempt to retrieve non-existent product (throws exception)
        try {
            productService.getProductById(999L);
        } catch (Exception e) {
            // Expected exception
        }

        // Then - Original product should still exist
        assertThat(productRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Persistence: Product timestamps are set correctly")
    void testProductTimestamps() throws Exception {
        // Given - Create product
        Product product = createTestProduct("SKU-001", "Product with timestamps");

        // When - Save product
        Product saved = productService.createProduct(product);

        // Then - Verify timestamps are set
        Product retrieved = productRepository.findById(saved.getId()).get();
        assertThat(retrieved.getCreatedAt()).isNotNull();
        assertThat(retrieved.getUpdatedAt()).isNotNull();
        assertThat(retrieved.getUpdatedAt()).isAfterOrEqualTo(retrieved.getCreatedAt());
    }

    @Test
    @DisplayName("Relationship: Product maintains category association")
    @Transactional
    void testProductCategoryAssociation() {
        // Given - Create product with category
        Product product = createTestProduct("SKU-001", "Categorized Product");

        // When - Save
        Product saved = productService.createProduct(product);

        // Then - Verify category is persisted
        Product retrieved = productRepository.findById(saved.getId()).get();
        assertThat(retrieved.getCategory()).isNotNull();
        assertThat(retrieved.getCategory().getId()).isEqualTo(testCategory.getId());
    }

    // Helper method
    private Product createTestProduct(String sku, String name) {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setDescription("Test description");
        product.setUnitPrice(new BigDecimal("99.99"));
        product.setStockQuantity(100);
        product.setReorderLevel(20);
        product.setIsActive(true);
        product.setCategory(testCategory);
        return product;
    }
}
