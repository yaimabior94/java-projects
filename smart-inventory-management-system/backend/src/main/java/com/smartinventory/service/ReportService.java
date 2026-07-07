package com.smartinventory.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.smartinventory.dto.InventoryReportDto;
import com.smartinventory.dto.LowStockProductDto;
import com.smartinventory.dto.PurchaseReportDto;
import com.smartinventory.dto.SalesReportDto;
import com.smartinventory.repository.ProductRepository;
import com.smartinventory.repository.PurchaseRepository;
import com.smartinventory.repository.SaleRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;

    public ReportService(ProductRepository productRepository,
                         SaleRepository saleRepository,
                         PurchaseRepository purchaseRepository) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryReportDto> getInventoryReport() {
        return productRepository.findInventoryReport();
    }

    @Transactional(readOnly = true)
    public List<SalesReportDto> getSalesReport() {
        return saleRepository.findSalesReport();
    }

    @Transactional(readOnly = true)
    public List<PurchaseReportDto> getPurchaseReport() {
        return purchaseRepository.findPurchaseReport();
    }

    @Transactional(readOnly = true)
    public List<LowStockProductDto> getLowStockReport() {
        return productRepository.findLowStockProducts();
    }

    @Transactional(readOnly = true)
    public byte[] exportPdf(String reportType) {
        ReportTable table = buildReportTable(reportType);
        try {
            return createPdf(table);
        } catch (DocumentException e) {
            throw new IllegalStateException("Unable to generate PDF report", e);
        }
    }

    @Transactional(readOnly = true)
    public byte[] exportExcel(String reportType) {
        ReportTable table = buildReportTable(reportType);
        try {
            return createExcel(table);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to generate Excel report", e);
        }
    }

    private ReportTable buildReportTable(String reportType) {
        if ("inventory".equalsIgnoreCase(reportType)) {
            return inventoryTable();
        }
        if ("sales".equalsIgnoreCase(reportType)) {
            return salesTable();
        }
        if ("purchases".equalsIgnoreCase(reportType) || "purchase".equalsIgnoreCase(reportType)) {
            return purchaseTable();
        }
        if ("low-stock".equalsIgnoreCase(reportType) || "lowStock".equalsIgnoreCase(reportType)) {
            return lowStockTable();
        }
        throw new IllegalArgumentException("Unsupported report type: " + reportType);
    }

    private ReportTable inventoryTable() {
        String[] headers = {"Product ID", "SKU", "Product", "Category", "Supplier", "Stock", "Reorder Level", "Unit Price", "Stock Value", "Active"};
        List<String[]> rows = new ArrayList<>();
        for (InventoryReportDto item : getInventoryReport()) {
            rows.add(new String[]{
                    stringValue(item.getProductId()),
                    item.getSku(),
                    item.getProductName(),
                    item.getCategoryName(),
                    item.getSupplierName(),
                    stringValue(item.getStockQuantity()),
                    stringValue(item.getReorderLevel()),
                    moneyValue(item.getUnitPrice()),
                    moneyValue(item.getStockValue()),
                    booleanValue(item.getActive())
            });
        }
        return new ReportTable("Inventory Report", "inventory-report", headers, rows);
    }

    private ReportTable salesTable() {
        String[] headers = {"Sale ID", "Invoice", "Customer", "Sale Date", "Total", "Discount", "Tax", "Payment Status", "Created By"};
        List<String[]> rows = new ArrayList<>();
        for (SalesReportDto item : getSalesReport()) {
            rows.add(new String[]{
                    stringValue(item.getSaleId()),
                    item.getInvoiceNumber(),
                    item.getCustomerName(),
                    dateValue(item.getSaleDate()),
                    moneyValue(item.getTotalAmount()),
                    moneyValue(item.getDiscount()),
                    moneyValue(item.getTax()),
                    item.getPaymentStatus(),
                    item.getCreatedBy()
            });
        }
        return new ReportTable("Sales Report", "sales-report", headers, rows);
    }

    private ReportTable purchaseTable() {
        String[] headers = {"Purchase ID", "Purchase No.", "Purchase Date", "Supplier", "Total", "Payment Status", "Created By"};
        List<String[]> rows = new ArrayList<>();
        for (PurchaseReportDto item : getPurchaseReport()) {
            rows.add(new String[]{
                    stringValue(item.getPurchaseId()),
                    item.getPurchaseNumber(),
                    dateValue(item.getPurchaseDate()),
                    item.getSupplierName(),
                    moneyValue(item.getTotalAmount()),
                    item.getPaymentStatus(),
                    item.getCreatedBy()
            });
        }
        return new ReportTable("Purchase Report", "purchase-report", headers, rows);
    }

    private ReportTable lowStockTable() {
        String[] headers = {"Product ID", "SKU", "Product", "Category", "Supplier", "Stock", "Reorder Level"};
        List<String[]> rows = new ArrayList<>();
        for (LowStockProductDto item : getLowStockReport()) {
            rows.add(new String[]{
                    stringValue(item.getId()),
                    item.getSku(),
                    item.getName(),
                    item.getCategoryName(),
                    item.getSupplierName(),
                    stringValue(item.getStockQuantity()),
                    stringValue(item.getReorderLevel())
            });
        }
        return new ReportTable("Low Stock Report", "low-stock-report", headers, rows);
    }

    private byte[] createPdf(ReportTable table) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph(table.getTitle(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(16);
        document.add(title);

        PdfPTable pdfTable = new PdfPTable(table.getHeaders().length);
        pdfTable.setWidthPercentage(100);
        addPdfHeaders(pdfTable, table.getHeaders());
        for (String[] row : table.getRows()) {
            for (String value : row) {
                pdfTable.addCell(safeValue(value));
            }
        }
        document.add(pdfTable);
        document.close();
        return outputStream.toByteArray();
    }

    private void addPdfHeaders(PdfPTable table, String[] headers) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private byte[] createExcel(ReportTable table) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // FIX BUG-19: Use try-with-resources so the workbook is always closed,
        // even when an exception is thrown mid-way through building the sheet.
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(table.getTitle());
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < table.getHeaders().length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(table.getHeaders()[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (String[] row : table.getRows()) {
                Row excelRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < row.length; i++) {
                    excelRow.createCell(i).setCellValue(safeValue(row[i]));
                }
            }

            for (int i = 0; i < table.getHeaders().length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        }
        return outputStream.toByteArray();
    }

    public String getFileName(String reportType, String extension) {
        return buildReportTable(reportType).getFileName() + "." + extension;
    }

    private String stringValue(Object value) {
        return value != null ? value.toString() : "";
    }

    private String moneyValue(BigDecimal value) {
        return value != null ? value.toPlainString() : "0.00";
    }

    private String dateValue(LocalDateTime value) {
        return value != null ? value.format(DATE_TIME_FORMATTER) : "";
    }

    private String booleanValue(Boolean value) {
        return Boolean.TRUE.equals(value) ? "Yes" : "No";
    }

    private String safeValue(String value) {
        return value != null ? value : "";
    }

    private static class ReportTable {
        private final String title;
        private final String fileName;
        private final String[] headers;
        private final List<String[]> rows;

        ReportTable(String title, String fileName, String[] headers, List<String[]> rows) {
            this.title = title;
            this.fileName = fileName;
            this.headers = headers;
            this.rows = rows;
        }

        String getTitle() {
            return title;
        }

        String getFileName() {
            return fileName;
        }

        String[] getHeaders() {
            return headers;
        }

        List<String[]> getRows() {
            return rows;
        }
    }
}
