package com.smartinventory.controller;

import com.smartinventory.entity.Purchase;
import com.smartinventory.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchases")
@Tag(name = "Purchases", description = "Purchase management endpoints")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    @Operation(summary = "Get all purchases")
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase by id")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new purchase and add stock")
    public ResponseEntity<Purchase> createPurchase(@Valid @RequestBody Purchase purchase) {
        return new ResponseEntity<>(purchaseService.createPurchase(purchase), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing purchase")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable Long id, @Valid @RequestBody Purchase purchase) {
        return ResponseEntity.ok(purchaseService.updatePurchase(id, purchase));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a purchase")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-stock")
    @Operation(summary = "Business Rule Case 1: Add stock from purchase (Stock = Stock + Quantity)")
    public ResponseEntity<Map<String, String>> addStockFromPurchase(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        purchaseService.addStockFromPurchase(productId, quantity);
        return ResponseEntity.ok(Map.of("message", "Stock added successfully for product " + productId + ". Quantity added: " + quantity));
    }
}
