import React, { useEffect, useState } from 'react';
import Table from '../components/Table';
import Modal from '../components/Modal';
import { purchasesApi, productsApi, suppliersApi } from '../services/api';
import PurchaseForm from '../components/Forms/PurchaseForm';

const EMPTY_PO = { supplierId: '' };
const EMPTY_ITEM = { productId: '', quantity: '1', unitCost: '' };

export default function Purchases() {
  const [purchases, setPurchases] = useState([]);
  const [products, setProducts] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingPurchase, setEditingPurchase] = useState(null);
  // BUG-13 FIX: error state for user-visible feedback
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [purchasesRes, productsRes, suppliersRes] = await Promise.all([
        purchasesApi.getAll(),
        productsApi.getAll(),
        suppliersApi.getAll()
      ]);
      setPurchases(purchasesRes.data);
      setProducts(productsRes.data);
      setSuppliers(suppliersRes.data);
    } catch (error) {
      console.error('Error fetching purchases:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingPurchase(null);
    setModalOpen(true);
  };

  const handleEdit = (purchase) => {
    setEditingPurchase(purchase);
    setModalOpen(true);
  };

  const handleDelete = async (purchase) => {
    if (window.confirm(`Are you sure you want to delete purchase #${purchase.id}?`)) {
      try {
        await purchasesApi.delete(purchase.id);
        fetchData();
      } catch (err) {
        console.error('Error deleting purchase:', err);
        // BUG-13 FIX: show error to user
        setError(err.response?.data?.message || err.message || 'Failed to delete purchase.');
      }
    }
  };

  const handleSubmit = async (formData) => {
    try {
      if (editingPurchase) {
        await purchasesApi.update(editingPurchase.id, formData);
      } else {
        await purchasesApi.create(formData);
      }
      setModalOpen(false);
      setError(null);
      fetchData();
    } catch (err) {
      console.error('Error saving purchase:', err);
      // BUG-13 FIX: show error to user inside the modal context
      setError(err.response?.data?.message || err.message || 'Failed to save purchase.');
    }
  };

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'supplierName', label: 'Supplier', render: (row) => row.supplier?.name || '-' },
    { key: 'totalAmount', label: 'Total', render: (row) => `$${parseFloat(row.totalAmount || 0).toFixed(2)}` },
    // BUG-08 FIX (also in Purchases list): API returns 'purchaseDate', not 'orderDate'
    { key: 'purchaseDate', label: 'Date', render: (row) => row.purchaseDate ? new Date(row.purchaseDate).toLocaleDateString() : '-' },
    { key: 'paymentStatus', label: 'Status' },
  ];

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#ffffff' }}>Loading...</div>;
  }

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h1 style={{ color: '#fff' }}>Purchases</h1>
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
          + New Purchase
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
        data={purchases}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editingPurchase ? 'Edit Purchase' : 'New Purchase'}
        size="lg"
      >
        <PurchaseForm
          purchase={editingPurchase}
          products={products}
          suppliers={suppliers}
          onSubmit={handleSubmit}
          onCancel={() => setModalOpen(false)}
        />
      </Modal>
    </div>
  );
}
