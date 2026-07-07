import React, { useEffect, useState } from 'react';
import Table from '../components/Table';
import Modal from '../components/Modal';
import { salesApi, productsApi } from '../services/api';
import SaleForm from '../components/Forms/SaleForm';

const EMPTY_SALE = { customerName: '', discount: '0', tax: '0' };
const EMPTY_ITEM = { productId: '', quantity: '1', unitPrice: '' };

export default function Sales() {
  const [sales, setSales] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingSale, setEditingSale] = useState(null);
  // BUG-13 FIX: error state for user-visible feedback
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [salesRes, productsRes] = await Promise.all([
        salesApi.getAll(),
        productsApi.getAll()
      ]);
      setSales(salesRes.data);
      setProducts(productsRes.data);
    } catch (error) {
      console.error('Error fetching sales:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingSale(null);
    setModalOpen(true);
  };

  const handleEdit = (sale) => {
    setEditingSale(sale);
    setModalOpen(true);
  };

  const handleDelete = async (sale) => {
    if (window.confirm(`Are you sure you want to delete sale #${sale.id}?`)) {
      try {
        await salesApi.delete(sale.id);
        fetchData();
      } catch (err) {
        console.error('Error deleting sale:', err);
        // BUG-13 FIX: show error to user
        setError(err.response?.data?.message || err.message || 'Failed to delete sale.');
      }
    }
  };

  const handleSubmit = async (formData) => {
    try {
      if (editingSale) {
        await salesApi.update(editingSale.id, formData);
      } else {
        await salesApi.create(formData);
      }
      setModalOpen(false);
      setError(null);
      fetchData();
    } catch (err) {
      console.error('Error saving sale:', err);
      // BUG-13 FIX: show error to user
      setError(err.response?.data?.message || err.message || 'Failed to save sale.');
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'customerName', label: 'Customer' },
    // BUG-H FIX: guard against null BigDecimal values returned from backend
    { key: 'totalAmount', label: 'Total', render: (row) => `$${parseFloat(row.totalAmount || 0).toFixed(2)}` },
    { key: 'discount', label: 'Discount', render: (row) => `$${parseFloat(row.discount || 0).toFixed(2)}` },
    { key: 'tax', label: 'Tax', render: (row) => `$${parseFloat(row.tax || 0).toFixed(2)}` },
    // BUG-I FIX: guard against null saleDate to avoid 'Invalid Date'
    { key: 'saleDate', label: 'Date', render: (row) => row.saleDate ? new Date(row.saleDate).toLocaleDateString() : '-' },
    { key: 'paymentStatus', label: 'Status' },
  ];

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#ffffff' }}>Loading...</div>;
  }

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h1 style={{ color: '#fff' }}>Sales</h1>
        <button
          onClick={handleAdd}
          style={{
            padding: '10px 20px',
            backgroundColor: '#3b82f6',
            color: '#fff',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer'
          }}
        >
          + New Sale
        </button>
      </div>

      {/* BUG-13 FIX: visible error message */}
      {error && (
        <div style={{
          padding: '12px 16px', marginBottom: '16px',
          backgroundColor: 'rgba(239,68,68,0.15)',
          border: '1px solid rgba(239,68,68,0.4)',
          borderRadius: '8px', color: '#fca5a5', fontSize: '0.9rem'
        }}>
          ⚠️ {error}
        </div>
      )}

      <Table
        columns={columns}
        data={sales}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editingSale ? 'Edit Sale' : 'New Sale'}
        size="lg"
      >
        <SaleForm
          sale={editingSale}
          products={products}
          onSubmit={handleSubmit}
          onCancel={() => setModalOpen(false)}
        />
      </Modal>
    </div>
  );
}
