package com.smartinventory.controller;

import com.smartinventory.dto.InventoryReportDto;
import com.smartinventory.dto.LowStockProductDto;
import com.smartinventory.dto.PurchaseReportDto;
import com.smartinventory.dto.SalesReportDto;
import com.smartinventory.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Report preview and export endpoints")
public class ReportController {

    private static final MediaType EXCEL_MEDIA_TYPE = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/inventory")
    @Operation(summary = "Get inventory report data")
    public ResponseEntity<List<InventoryReportDto>> getInventoryReport() {
        return ResponseEntity.ok(reportService.getInventoryReport());
    }

    @GetMapping("/sales")
    @Operation(summary = "Get sales report data")
    public ResponseEntity<List<SalesReportDto>> getSalesReport() {
        return ResponseEntity.ok(reportService.getSalesReport());
    }

    @GetMapping("/purchases")
    @Operation(summary = "Get purchase report data")
    public ResponseEntity<List<PurchaseReportDto>> getPurchaseReport() {
        return ResponseEntity.ok(reportService.getPurchaseReport());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock report data")
    public ResponseEntity<List<LowStockProductDto>> getLowStockReport() {
        return ResponseEntity.ok(reportService.getLowStockReport());
    }

    @GetMapping("/{reportType}/pdf")
    @Operation(summary = "Download report as PDF")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String reportType) {
        byte[] file = reportService.exportPdf(reportType);
        return download(file, reportService.getFileName(reportType, "pdf"), MediaType.APPLICATION_PDF);
    }

    @GetMapping("/{reportType}/excel")
    @Operation(summary = "Download report as Excel")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable String reportType) {
        byte[] file = reportService.exportExcel(reportType);
        return download(file, reportService.getFileName(reportType, "xlsx"), EXCEL_MEDIA_TYPE);
    }

    @SuppressWarnings("null")
    private ResponseEntity<byte[]> download(byte[] file, String fileName, MediaType mediaType) {
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString())
                .body(file);
    }
}
