package com.smartinventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartinventory.entity.Category;
import com.smartinventory.entity.Product;
import com.smartinventory.service.ProductService;
import com.smartinventory.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests for ProductController using @WebMvcTest.
 * Security auto-configuration is excluded so that we don't get 401/403 errors during MVC isolation testing.
 */
@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@DisplayName("Product Controller Tests")
@SuppressWarnings("null")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private com.smartinventory.security.JwtService jwtService;

    @MockBean
    private com.smartinventory.security.UserDetailsServiceImpl userDetailsService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category(1L, "Electronics", "Electronic products");
        testProduct = createTestProduct(1L, "SKU-001", "Test Product");
    }

    @Test
    @DisplayName("GET /api/products - Should return all products with 200 OK")
    void testGetAllProducts() throws Exception {
        // Given - Service returns list of products
        List<Product> products = Arrays.asList(
                createTestProduct(1L, "SKU-001", "Product 1"),
                createTestProduct(2L, "SKU-002", "Product 2")
        );
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then - Perform request and verify response
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo("Product 1")))
                .andExpect(jsonPath("$[1].name", equalTo("Product 2")));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("GET /api/products - Should return empty array when no products exist")
    void testGetAllProducts_Empty() throws Exception {
        // Given - Service returns empty list
        when(productService.getAllProducts()).thenReturn(Arrays.asList());

        // When & Then - Verify empty response
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return product with 200 OK")
    void testGetProductById_Success() throws Exception {
        // Given - Service returns product
        when(productService.getProductById(1L)).thenReturn(testProduct);

        // When & Then - Verify response
        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.sku", equalTo("SKU-001")))
                .andExpect(jsonPath("$.name", equalTo("Test Product")))
                .andExpect(jsonPath("$.unitPrice", equalTo(99.99)));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("GET /api/products/{id} - Should return 404 when product not found")
    void testGetProductById_NotFound() throws Exception {
        // Given - Service throws exception
        when(productService.getProductById(999L))
                .thenThrow(new ResourceNotFoundException("Product not found"));

        // When & Then - Verify 404 response
        mockMvc.perform(get("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(999L);
    }

    @Test
    @DisplayName("GET /api/products/search?name=query - Should return matching products")
    void testSearchProducts() throws Exception {
        // Given - Service returns matching products
        List<Product> matchingProducts = Arrays.asList(
                createTestProduct(1L, "SKU-001", "Laptop Computer"),
                createTestProduct(2L, "SKU-002", "Desktop Computer")
        );
        when(productService.searchProductsByName("Computer"))
                .thenReturn(matchingProducts);

        // When & Then - Verify response
        mockMvc.perform(get("/api/products/search")
                        .param("name", "Computer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", hasItems(
                        containsString("Computer"),
                        containsString("Computer"))));

        verify(productService, times(1)).searchProductsByName("Computer");
    }

    @Test
    @DisplayName("POST /api/products - Should create product with 201 CREATED")
    void testCreateProduct_Success() throws Exception {
        // Given - Product to create
        Product newProduct = createTestProduct(null, "SKU-NEW", "New Product");
        Product savedProduct = createTestProduct(1L, "SKU-NEW", "New Product");
        when(productService.createProduct(any(Product.class)))
                .thenReturn(savedProduct);

        // When & Then - Perform POST and verify 201
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.sku", equalTo("SKU-NEW")))
                .andExpect(jsonPath("$.name", equalTo("New Product")));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("POST /api/products - Should validate required fields")
    void testCreateProduct_ValidationError() throws Exception {
        // Given - Product with missing required fields
        String invalidProduct = "{\"sku\": \"\"}"; // Missing name, unitPrice, etc.

        // When & Then - Verify 400 Bad Request
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProduct))
                .andExpect(status().isBadRequest());

        // Service should not be called for invalid input
        verify(productService, never()).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Should update product with 200 OK")
    void testUpdateProduct_Success() throws Exception {
        // Given - Updated product data
        Product updatedProduct = createTestProduct(1L, "SKU-UPDATED", "Updated Product");
        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(updatedProduct);

        // When & Then - Perform PUT and verify response
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", equalTo("SKU-UPDATED")))
                .andExpect(jsonPath("$.name", equalTo("Updated Product")));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Should return 404 when product not found")
    void testUpdateProduct_NotFound() throws Exception {
        // Given - Service throws exception
        when(productService.updateProduct(eq(999L), any(Product.class)))
                .thenThrow(new ResourceNotFoundException("Product not found"));

        // When & Then - Verify 404 response
        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(999L), any(Product.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Should delete product with 204 NO CONTENT")
    void testDeleteProduct_Success() throws Exception {
        // When & Then - Perform DELETE
        mockMvc.perform(delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Should return 404 when product not found")
    void testDeleteProduct_NotFound() throws Exception {
        // Given - Service throws exception
        doThrow(new ResourceNotFoundException("Product not found"))
                .when(productService).deleteProduct(999L);

        // When & Then - Verify 404 response
        mockMvc.perform(delete("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteProduct(999L);
    }

    // Helper method to create test product
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
}
