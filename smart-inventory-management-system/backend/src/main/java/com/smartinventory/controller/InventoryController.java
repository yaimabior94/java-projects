package com.smartinventory.controller;

import com.smartinventory.entity.Inventory;
import com.smartinventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "Inventory management endpoints")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @Operation(summary = "Get all inventory records")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by id")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory by product id")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @PostMapping
    @Operation(summary = "Create a new inventory record")
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        return new ResponseEntity<>(inventoryService.createInventory(inventory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory record")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @Valid @RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventory));
    }

    @PostMapping("/stock/add")
    @Operation(summary = "Add stock to a product")
    public ResponseEntity<String> addStock(@RequestParam Long productId, @RequestParam Integer quantity) {
        inventoryService.addStock(productId, quantity);
        return ResponseEntity.ok("Stock added successfully");
    }

    @PostMapping("/stock/reduce")
    @Operation(summary = "Reduce stock for a product")
    public ResponseEntity<String> reduceStock(@RequestParam Long productId, @RequestParam Integer quantity) {
        inventoryService.reduceStock(productId, quantity);
        return ResponseEntity.ok("Stock reduced successfully");
    }
}
