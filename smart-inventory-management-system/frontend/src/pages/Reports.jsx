import React, { useEffect, useState } from 'react';
import Table from '../components/Table';
import { reportsApi } from '../services/api';

const REPORT_TYPES = [
  { key: 'inventory', label: 'Inventory Report', icon: '📦', desc: 'All products with stock levels, prices and stock value.' },
  { key: 'sales', label: 'Sales Report', icon: '💰', desc: 'Complete sales history with payment status breakdown.' },
  { key: 'purchases', label: 'Purchase Report', icon: '🛒', desc: 'All purchase orders from suppliers with amounts.' },
  { key: 'low-stock', label: 'Low Stock Report', icon: '⚠️', desc: 'Products at or below their reorder level threshold.' },
];

const saveBlob = (blob, filename) => {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  a.click();
  window.URL.revokeObjectURL(url);
};

export default function Reports() {
  const [selectedReport, setSelectedReport] = useState(null);
  const [reportData, setReportData] = useState([]);
  const [loading, setLoading] = useState(false);
  // BUG-13 FIX: error state for user-visible feedback
  const [error, setError] = useState(null);

  const fetchReport = async (reportType) => {
    setSelectedReport(reportType);
    setLoading(true);
    setError(null);
    try {
      let res;
      switch (reportType) {
        case 'inventory':
          res = await reportsApi.getInventory();
          break;
        case 'sales':
          res = await reportsApi.getSales();
          break;
        case 'purchases':
          res = await reportsApi.getPurchases();
          break;
        case 'low-stock':
          res = await reportsApi.getLowStock();
          break;
        default:
          return;
      }
      setReportData(res.data);
    } catch (err) {
      console.error('Error fetching report:', err);
      setError(err.response?.data?.message || err.message || 'Failed to load report data.');
    } finally {
      setLoading(false);
    }
  };

  const handleDownloadPdf = async () => {
    if (!selectedReport) return;
    try {
      const res = await reportsApi.downloadPdf(selectedReport);
      saveBlob(res.data, `${selectedReport}-report.pdf`);
    } catch (err) {
      console.error('Error downloading PDF:', err);
      window.alert(err.response?.data?.message || 'Failed to download PDF.');
    }
  };

  const handleDownloadExcel = async () => {
    if (!selectedReport) return;
    try {
      const res = await reportsApi.downloadExcel(selectedReport);
      saveBlob(res.data, `${selectedReport}-report.xlsx`);
    } catch (err) {
      console.error('Error downloading Excel:', err);
      window.alert(err.response?.data?.message || 'Failed to download Excel file.');
    }
  };

  const getColumns = () => {
    switch (selectedReport) {
      case 'inventory':
        return [
          { key: 'productName', label: 'Product' },
          { key: 'sku', label: 'SKU' },
          // BUG-07 FIX: was 'currentStock', API returns 'stockQuantity'
          { key: 'stockQuantity', label: 'Stock' },
          { key: 'stockValue', label: 'Value', render: (row) => `$${parseFloat(row.stockValue || 0).toFixed(2)}` },
        ];
      case 'sales':
        return [
          // BUG-09 FIX: was 'id', SalesReportDto uses 'saleId'
          { key: 'saleId', label: 'ID' },
          { key: 'customerName', label: 'Customer' },
          { key: 'totalAmount', label: 'Total', render: (row) => `$${parseFloat(row.totalAmount || 0).toFixed(2)}` },
          { key: 'saleDate', label: 'Date', render: (row) => row.saleDate ? new Date(row.saleDate).toLocaleDateString() : '-' },
        ];
      case 'purchases':
        return [
          { key: 'purchaseId', label: 'ID' },
          { key: 'supplierName', label: 'Supplier' },
          { key: 'totalAmount', label: 'Total', render: (row) => `$${parseFloat(row.totalAmount || 0).toFixed(2)}` },
          // BUG-08 FIX: was 'orderDate', PurchaseReportDto uses 'purchaseDate'
          { key: 'purchaseDate', label: 'Date', render: (row) => row.purchaseDate ? new Date(row.purchaseDate).toLocaleDateString() : '-' },
        ];
      case 'low-stock':
        return [
          { key: 'name', label: 'Product' },
          { key: 'sku', label: 'SKU' },
          // BUG-07 FIX: was 'currentStock', LowStockProductDto uses 'stockQuantity'
          { key: 'stockQuantity', label: 'Current Stock' },
          { key: 'reorderLevel', label: 'Reorder Level' },
        ];
      default:
        return [];
    }
  };

  return (
    <div style={{ padding: '24px' }}>
      <h1 style={{ color: '#fff', marginBottom: '24px' }}>Reports</h1>

      {!selectedReport ? (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '20px' }}>
          {REPORT_TYPES.map((report) => (
            <div
              key={report.key}
              onClick={() => fetchReport(report.key)}
              style={{
                padding: '24px',
                backgroundColor: 'rgba(59, 130, 246, 0.1)',
                border: '1px solid rgba(59, 130, 246, 0.3)',
                borderRadius: '12px',
                cursor: 'pointer',
                transition: 'all 0.2s'
              }}
              // BUG-20 FIX: use e.currentTarget instead of e.target so the hover style
              // applies to the card container, not whichever child element was hovered.
              onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'rgba(59, 130, 246, 0.2)'}
              onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'rgba(59, 130, 246, 0.1)'}
            >
              <div style={{ fontSize: '2rem', marginBottom: '12px' }}>{report.icon}</div>
              <h3 style={{ color: '#fff', marginBottom: '8px' }}>{report.label}</h3>
              <p style={{ color: '#9ca3af', fontSize: '0.9rem' }}>{report.desc}</p>
            </div>
          ))}
        </div>
      ) : (
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
            <button
              onClick={() => { setSelectedReport(null); setError(null); }}
              style={{
                padding: '8px 16px',
                backgroundColor: '#6b7280',
                color: '#fff',
                border: 'none',
                borderRadius: '6px',
                cursor: 'pointer'
              }}
            >
              ← Back to Reports
            </button>
            <div style={{ display: 'flex', gap: '12px' }}>
              <button
                onClick={handleDownloadPdf}
                style={{
                  padding: '8px 16px',
                  backgroundColor: '#ef4444',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '6px',
                  cursor: 'pointer'
                }}
              >
                Download PDF
              </button>
              <button
                onClick={handleDownloadExcel}
                style={{
                  padding: '8px 16px',
                  backgroundColor: '#10b981',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '6px',
                  cursor: 'pointer'
                }}
              >
                Download Excel
              </button>
            </div>
          </div>

          {/* BUG-13 FIX: show error message to user */}
          {error && (
            <div style={{
              padding: '12px 16px',
              marginBottom: '16px',
              backgroundColor: 'rgba(239, 68, 68, 0.15)',
              border: '1px solid rgba(239, 68, 68, 0.4)',
              borderRadius: '8px',
              color: '#fca5a5',
              fontSize: '0.9rem'
            }}>
              ⚠️ {error}
            </div>
          )}

          {loading ? (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '200px', color: '#ffffff' }}>Loading...</div>
          ) : (
            <Table columns={getColumns()} data={reportData} />
          )}
        </div>
      )}
    </div>
  );
}
