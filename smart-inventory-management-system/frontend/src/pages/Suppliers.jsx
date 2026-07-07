import React, { useEffect, useState } from 'react';
import Table from '../components/Table';
import Modal from '../components/Modal';
import SupplierForm from '../components/Forms/SupplierForm';
import { suppliersApi } from '../services/api';

const EMPTY = { name: '', contactPerson: '', phone: '', email: '', address: '' };

export default function Suppliers() {
  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingSupplier, setEditingSupplier] = useState(null);
  // BUG-13 FIX: error state for user-visible feedback
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const res = await suppliersApi.getAll();
      setSuppliers(res.data);
    } catch (error) {
      console.error('Error fetching suppliers:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingSupplier(null);
    setModalOpen(true);
  };

  const handleEdit = (supplier) => {
    setEditingSupplier(supplier);
    setModalOpen(true);
  };

  const handleDelete = async (supplier) => {
    if (window.confirm(`Are you sure you want to delete ${supplier.name}?`)) {
      try {
        await suppliersApi.delete(supplier.id);
        fetchData();
      } catch (err) {
        console.error('Error deleting supplier:', err);
        // BUG-13 FIX: show error to user
        setError(err.response?.data?.message || err.message || 'Failed to delete supplier.');
      }
    }
  };

  const handleSubmit = async (formData) => {
    try {
      if (editingSupplier) {
        await suppliersApi.update(editingSupplier.id, formData);
      } else {
        await suppliersApi.create(formData);
      }
      setModalOpen(false);
      setError(null);
      fetchData();
    } catch (err) {
      console.error('Error saving supplier:', err);
      // BUG-13 FIX: show error to user
      setError(err.response?.data?.message || err.message || 'Failed to save supplier.');
    }
  };

  const columns = [
    { key: 'name', label: 'Name' },
    { key: 'contactPerson', label: 'Contact Person' },
    { key: 'email', label: 'Email' },
    { key: 'phone', label: 'Phone' },
    { key: 'address', label: 'Address' },
  ];

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#ffffff' }}>Loading...</div>;
  }

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h1 style={{ color: '#fff' }}>Suppliers</h1>
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
          + Add Supplier
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
        data={suppliers}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editingSupplier ? 'Edit Supplier' : 'New Supplier'}
      >
        <SupplierForm
          supplier={editingSupplier}
          onSubmit={handleSubmit}
          onCancel={() => setModalOpen(false)}
        />
      </Modal>
    </div>
  );
}
