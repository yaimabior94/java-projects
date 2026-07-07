package com.smartinventory.controller;

import com.smartinventory.dto.DashboardSummaryDto;
import com.smartinventory.dto.LowStockProductDto;
import com.smartinventory.dto.MonthlyTotalDto;
import com.smartinventory.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard reporting endpoints")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @Operation(summary = "Get dashboard summary")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    @GetMapping("/low-stock-products")
    @Operation(summary = "Get low stock products")
    public ResponseEntity<List<LowStockProductDto>> getLowStockProducts() {
        return ResponseEntity.ok(dashboardService.getLowStockProducts());
    }

    @GetMapping("/monthly-sales")
    @Operation(summary = "Get monthly sales totals")
    public ResponseEntity<List<MonthlyTotalDto>> getMonthlySales() {
        return ResponseEntity.ok(dashboardService.getMonthlySales());
    }

    @GetMapping("/monthly-purchases")
    @Operation(summary = "Get monthly purchase totals")
    public ResponseEntity<List<MonthlyTotalDto>> getMonthlyPurchases() {
        return ResponseEntity.ok(dashboardService.getMonthlyPurchases());
    }
}
