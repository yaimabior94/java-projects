package com.smartinventory.service;

import com.smartinventory.entity.Category;
import com.smartinventory.entity.Product;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Service layer tests for ProductService using Mockito
 *
 * @ExtendWith(MockitoExtension.class) - Enables Mockito annotations
 * @Mock - Creates a mock of ProductRepository
 * @InjectMocks - Injects mocked repository into ProductService
 *
 * Benefits of mocking:
 * - Tests service logic in isolation
 * - No database calls needed
 * - Fast test execution
 * - Easy to simulate exception scenarios
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Create test data
        testCategory = new Category(1L, "Electronics", "Electronic products");
        testProduct = createTestProduct(1L, "SKU-001", "Test Product");
    }

    @Test
    @DisplayName("Should retrieve all products")
    void testGetAllProducts() {
        // Given - Mock repository returns list of products
        List<Product> productList = Arrays.asList(
                createTestProduct(1L, "SKU-001", "Product 1"),
                createTestProduct(2L, "SKU-002", "Product 2")
        );
        when(productRepository.findAll()).thenReturn(productList);

        // When - Call service method
        List<Product> result = productService.getAllProducts();

        // Then - Verify results and mock was called
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name").contains("Product 1", "Product 2");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void testGetAllProducts_Empty() {
        // Given - Mock repository returns empty list
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // When - Call service method
        List<Product> result = productService.getAllProducts();

        // Then - Should return empty list
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get product by id successfully")
    void testGetProductById_Success() {
        // Given - Mock repository returns a product
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When - Call service method
        Product result = productService.getProductById(1L);

        // Then - Verify result and interaction
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found")
    void testGetProductById_NotFound() {
        // Given - Mock repository returns empty Optional
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then - Should throw exception
        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct_Success() {
        // Given - Product to create
        Product newProduct = createTestProduct(null, "SKU-NEW", "New Product");
        Product savedProduct = createTestProduct(1L, "SKU-NEW", "New Product");
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When - Call service method
        Product result = productService.createProduct(newProduct);

        // Then - Verify creation
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getSku()).isEqualTo("SKU-NEW");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct_Success() {
        // Given - Existing product and update data
        Product updatedData = new Product();
        updatedData.setSku("SKU-UPDATED");
        updatedData.setName("Updated Product");
        updatedData.setDescription("Updated Description");
        updatedData.setUnitPrice(new BigDecimal("199.99"));
        updatedData.setStockQuantity(50);
        updatedData.setReorderLevel(10);
        updatedData.setIsActive(true);
        updatedData.setCategory(testCategory);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When - Call service method
        Product result = productService.updateProduct(1L, updatedData);

        // Then - Verify update
        assertThat(result).isNotNull();
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent product")
    void testUpdateProduct_NotFound() {
        // Given - Non-existent product
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then - Should throw exception
        assertThatThrownBy(() -> productService.updateProduct(999L, testProduct))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProduct_Success() {
        // Given - Existing product
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(testProduct);

        // When - Call service method
        productService.deleteProduct(1L);

        // Then - Verify deletion
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent product")
    void testDeleteProduct_NotFound() {
        // Given - Non-existent product
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then - Should throw exception
        assertThatThrownBy(() -> productService.deleteProduct(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should find product by SKU")
    void testFindBySku_Success() {
        // Given - Mock repository returns product
        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.of(testProduct));

        // When - Call service method
        Optional<Product> result = productService.findBySku("SKU-001");

        // Then - Verify result
        assertThat(result).isPresent();
        assertThat(result.get().getSku()).isEqualTo("SKU-001");
        verify(productRepository, times(1)).findBySku("SKU-001");
    }

    @Test
    @DisplayName("Should return empty when SKU not found")
    void testFindBySku_NotFound() {
        // Given - Mock repository returns empty
        when(productRepository.findBySku("NON-EXISTENT")).thenReturn(Optional.empty());

        // When - Call service method
        Optional<Product> result = productService.findBySku("NON-EXISTENT");

        // Then - Verify result
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findBySku("NON-EXISTENT");
    }

    @Test
    @DisplayName("Should search products by name")
    void testSearchProductsByName() {
        // Given - Mock repository returns matching products
        List<Product> matchingProducts = Arrays.asList(
                createTestProduct(1L, "SKU-001", "Laptop Computer"),
                createTestProduct(2L, "SKU-002", "Desktop Computer")
        );
        when(productRepository.findByNameContainingIgnoreCase("Computer"))
                .thenReturn(matchingProducts);

        // When - Call service method
        List<Product> result = productService.searchProductsByName("Computer");

        // Then - Verify results
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name")
                .allMatch(name -> ((String) name).contains("Computer"));
        verify(productRepository, times(1))
                .findByNameContainingIgnoreCase("Computer");
    }

    @Test
    @DisplayName("Should return empty when search finds no products")
    void testSearchProductsByName_NoResults() {
        // Given - Mock repository returns empty
        when(productRepository.findByNameContainingIgnoreCase("NonExistent"))
                .thenReturn(Arrays.asList());

        // When - Call service method
        List<Product> result = productService.searchProductsByName("NonExistent");

        // Then - Verify empty result
        assertThat(result).isEmpty();
        verify(productRepository, times(1))
                .findByNameContainingIgnoreCase("NonExistent");
    }

    @Test
    @DisplayName("Should retrieve low stock products")
    void testGetLowStockProducts() {
        // Given - Mock repository returns low stock products
        List<Product> lowStockProducts = Arrays.asList(
                createLowStockProduct(1L, "Product 1", 5),
                createLowStockProduct(2L, "Product 2", 8)
        );
        when(productRepository.findByStockQuantityLessThanEqual(10))
                .thenReturn(lowStockProducts);

        // When - Call service method
        List<Product> result = productService.getLowStockProducts(10);

        // Then - Verify results
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(p -> p.getStockQuantity() <= 10);
        verify(productRepository, times(1))
                .findByStockQuantityLessThanEqual(10);
    }

    @Test
    @DisplayName("Should return empty when no low stock products exist")
    void testGetLowStockProducts_NoResults() {
        // Given - Mock repository returns empty
        when(productRepository.findByStockQuantityLessThanEqual(10))
                .thenReturn(Arrays.asList());

        // When - Call service method
        List<Product> result = productService.getLowStockProducts(10);

        // Then - Verify empty result
        assertThat(result).isEmpty();
        verify(productRepository, times(1))
                .findByStockQuantityLessThanEqual(10);
    }

    // Helper methods to create test data
    private Product createTestProduct(Long id, String sku, String name) {
        Product product = new Product();
        product.setId(id);
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

    private Product createLowStockProduct(Long id, String name, Integer quantity) {
        Product product = createTestProduct(id, "SKU-" + id, name);
        product.setStockQuantity(quantity);
        return product;
    }
}
