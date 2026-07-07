import React, { useEffect, useState } from 'react';
import Table from '../components/Table';
import Modal from '../components/Modal';
import { inventoryApi } from '../services/api';

export default function Inventory() {
  const [inventory, setInventory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  // BUG-13 FIX: error state for user-visible feedback
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const res = await inventoryApi.getAll();
      setInventory(res.data);
    } catch (error) {
      console.error('Error fetching inventory:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setModalOpen(true);
  };

  const handleSubmit = async (formData) => {
    try {
      await inventoryApi.update(editingItem.id, formData);
      setModalOpen(false);
      setError(null);
      fetchData();
    } catch (err) {
      console.error('Error updating inventory:', err);
      // BUG-13 FIX: show error to user
      setError(err.response?.data?.message || err.message || 'Failed to update inventory.');
    }
  };

  const columns = [
    { key: 'productName', label: 'Product' },
    { key: 'sku', label: 'SKU' },
    { key: 'currentStock', label: 'Current Stock' },
    { key: 'reorderLevel', label: 'Reorder Level' },
    { key: 'stockValue', label: 'Stock Value', render: (row) => `$${parseFloat(row.stockValue).toFixed(2)}` },
    { key: 'lastUpdated', label: 'Last Updated', render: (row) => new Date(row.lastUpdated).toLocaleDateString() },
  ];

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#ffffff' }}>Loading...</div>;
  }

  return (
    <div style={{ padding: '24px' }}>
      <h1 style={{ color: '#fff', marginBottom: '24px' }}>Inventory</h1>

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
        data={inventory}
        onEdit={handleEdit}
      />

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title="Edit Inventory"
      >
        <div style={{ padding: '20px' }}>
          <p>Inventory edit form implementation would go here</p>
          <button onClick={() => setModalOpen(false)}>Close</button>
        </div>
      </Modal>
    </div>
  );
}
