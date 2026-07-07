import React, { useEffect, useState } from 'react';
import Table from '../components/Table';
import Modal from '../components/Modal';
import ProductForm from '../components/Forms/ProductForm';
import { productsApi, categoriesApi, suppliersApi } from '../services/api';

const EMPTY = {
  sku: '', name: '', description: '', unitPrice: '',
  stockQuantity: '', reorderLevel: '', categoryId: '', supplierId: '', isActive: true,
};

export default function Products() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  // BUG-13 FIX: error state for user-visible feedback
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [productsRes, categoriesRes, suppliersRes] = await Promise.all([
        productsApi.getAll(),
        categoriesApi.getAll(),
        suppliersApi.getAll()
      ]);
      setProducts(productsRes.data);
      setCategories(categoriesRes.data);
      setSuppliers(suppliersRes.data);
    } catch (error) {
      console.error('Error fetching products:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingProduct(null);
    setModalOpen(true);
  };

  const handleEdit = (product) => {
    setEditingProduct(product);
    setModalOpen(true);
  };

  const handleDelete = async (product) => {
    if (window.confirm(`Are you sure you want to delete ${product.name}?`)) {
      try {
        await productsApi.delete(product.id);
        fetchData();
      } catch (err) {
        console.error('Error deleting product:', err);
        // BUG-13 FIX: show error to user
        setError(err.response?.data?.message || err.message || 'Failed to delete product.');
      }
    }
  };

  const handleSubmit = async (formData) => {
    try {
      if (editingProduct) {
        await productsApi.update(editingProduct.id, formData);
      } else {
        await productsApi.create(formData);
      }
      setModalOpen(false);
      setError(null);
      fetchData();
    } catch (err) {
      console.error('Error saving product:', err);
      // BUG-13 FIX: show error to user
      setError(err.response?.data?.message || err.message || 'Failed to save product.');
    }
  };

  const columns = [
    { key: 'sku', label: 'SKU' },
    { key: 'name', label: 'Name' },
    { key: 'categoryName', label: 'Category', render: (row) => row.category?.name || '-' },
    { key: 'supplierName', label: 'Supplier', render: (row) => row.supplier?.name || '-' },
    { key: 'unitPrice', label: 'Price', render: (row) => `$${parseFloat(row.unitPrice).toFixed(2)}` },
    { key: 'stockQuantity', label: 'Stock' },
    { key: 'reorderLevel', label: 'Reorder Level' },
  ];

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#ffffff' }}>Loading...</div>;
  }

  return (
    <div style={{ padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h1 style={{ color: '#fff' }}>Products</h1>
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
          + Add Product
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
        data={products}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editingProduct ? 'Edit Product' : 'New Product'}
      >
        <ProductForm
          product={editingProduct}
          onSubmit={handleSubmit}
          onCancel={() => setModalOpen(false)}
        />
      </Modal>
    </div>
  );
}
