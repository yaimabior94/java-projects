package com.smartinventory.service;

import com.smartinventory.entity.Product;
import com.smartinventory.entity.Sale;
import com.smartinventory.entity.SaleItem;
import com.smartinventory.exception.InsufficientStockException;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Sale Service Tests")
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SaleService saleService;

    private Product testProduct;
    private Sale testSale;
    private SaleItem testItem;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setStockQuantity(100);
        testProduct.setUnitPrice(new BigDecimal("10.00"));

        testItem = new SaleItem();
        testItem.setProduct(testProduct);
        testItem.setQuantity(5);
        testItem.setUnitPrice(new BigDecimal("10.00"));
        testItem.setLineTotal(new BigDecimal("50.00"));

        testSale = new Sale();
        testSale.setInvoiceNumber("INV-001");
        testSale.setSaleItems(new ArrayList<>(Collections.singletonList(testItem)));
    }

    @Test
    @DisplayName("Should successfully create a sale, update product stock and set parent reference on items")
    void testCreateSale_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(saleRepository.save(any(Sale.class))).thenReturn(testSale);

        // When
        Sale result = saleService.createSale(testSale);

        // Then
        assertThat(result).isNotNull();
        assertThat(testProduct.getStockQuantity()).isEqualTo(95); // 100 - 5 = 95
        assertThat(testItem.getSale()).isEqualTo(testSale); // Parent reference set!
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(testProduct);
        verify(saleRepository, times(1)).save(testSale);
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when sale quantity exceeds stock")
    void testCreateSale_InsufficientStock() {
        // Given
        testItem.setQuantity(150); // Exceeds stock (100)
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        assertThatThrownBy(() -> saleService.createSale(testSale))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock");

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any());
        verify(saleRepository, never()).save(any());
    }
}
