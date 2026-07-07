package com.smartinventory.service;

import com.smartinventory.entity.Product;
import com.smartinventory.entity.Purchase;
import com.smartinventory.entity.PurchaseItem;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.PurchaseRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Purchase Service Tests")
class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    private Product testProduct;
    private Purchase testPurchase;
    private PurchaseItem testItem;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setStockQuantity(100);
        testProduct.setUnitPrice(new BigDecimal("10.00"));

        testItem = new PurchaseItem();
        testItem.setProduct(testProduct);
        testItem.setQuantity(20);
        testItem.setUnitCost(new BigDecimal("8.00"));
        testItem.setLineTotal(new BigDecimal("160.00"));

        testPurchase = new Purchase();
        testPurchase.setPurchaseNumber("PO-001");
        testPurchase.setPurchaseItems(new ArrayList<>(Collections.singletonList(testItem)));
    }

    @Test
    @DisplayName("Should successfully create a purchase, increase product stock and set parent reference on items")
    void testCreatePurchase_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase);

        // When
        Purchase result = purchaseService.createPurchase(testPurchase);

        // Then
        assertThat(result).isNotNull();
        assertThat(testProduct.getStockQuantity()).isEqualTo(120); // 100 + 20 = 120
        assertThat(testItem.getPurchase()).isEqualTo(testPurchase); // Parent reference set!
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(testProduct);
        verify(purchaseRepository, times(1)).save(testPurchase);
    }
}
