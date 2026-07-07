import React, { useEffect, useState } from 'react';
import Table from '../components/Table';
import Modal from '../components/Modal';
import CategoryForm from '../components/Forms/CategoryForm';
import { categoriesApi } from '../services/api';

const EMPTY = { name: '', description: '' };

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState(null);
  // BUG-13 FIX: error state for user-visible feedback
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const res = await categoriesApi.getAll();
      setCategories(res.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingCategory(null);
    setModalOpen(true);
  };

  const handleEdit = (category) => {
    setEditingCategory(category);
    setModalOpen(true);
  };

  const handleDelete = async (category) => {
    if (window.confirm(`Are you sure you want to delete ${category.name}?`)) {
      try {
        await categoriesApi.delete(category.id);
        fetchData();
      } catch (err) {
        console.error('Error deleting category:', err);
        // BUG-13 FIX: show error to user
        setError(err.response?.data?.message || err.message || 'Failed to delete category.');
      }
    }
  };

  const handleSubmit = async (formData) => {
    try {
      if (editingCategory) {
        await categoriesApi.update(editingCategory.id, formData);
      } else {
        await categoriesApi.create(formData);
      }
      setModalOpen(false);
      setError(null);
      fetchData();
    } catch (err) {
      console.error('Error saving category:', err);
      // BUG-13 FIX: show error to user
      setError(err.response?.data?.message || err.message || 'Failed to save category.');
    }
  };

  const columns = [
    { key: 'name', label: 'Name' },
    { key: 'description', label: 'Description' },
  ];

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#ffffff' }}>Loading...</div>;
  }

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h1 style={{ color: '#fff' }}>Categories</h1>
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
          + Add Category
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
        data={categories}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editingCategory ? 'Edit Category' : 'New Category'}
      >
        <CategoryForm
          category={editingCategory}
          onSubmit={handleSubmit}
          onCancel={() => setModalOpen(false)}
        />
      </Modal>
    </div>
  );
}
