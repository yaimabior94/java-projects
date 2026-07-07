package com.smartinventory.controller;

import com.smartinventory.entity.Sale;
import com.smartinventory.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales", description = "Sales management endpoints")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    @Operation(summary = "Get all sales")
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by id")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new sale and reduce stock with validation")
    public ResponseEntity<Sale> createSale(@Valid @RequestBody Sale sale) {
        return new ResponseEntity<>(saleService.createSale(sale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing sale")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @Valid @RequestBody Sale sale) {
        return ResponseEntity.ok(saleService.updateSale(id, sale));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sale")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reduce-stock")
    @Operation(summary = "Business Rule Case 2: Reduce stock from sale (Stock = Stock - Quantity). Throws exception if insufficient stock.")
    public ResponseEntity<Map<String, String>> reduceStockFromSale(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        saleService.reduceStockFromSale(productId, quantity);
        return ResponseEntity.ok(Map.of("message", "Stock reduced successfully for product " + productId + ". Quantity sold: " + quantity));
    }
}
