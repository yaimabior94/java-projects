import React, { useState, useEffect } from 'react';
import { categoriesApi, suppliersApi } from '../../services/api';
import '../../styles/Forms.css';

const ProductForm = ({ product, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    name: product?.name || '',
    sku: product?.sku || '',
    description: product?.description || '',
    price: product?.unitPrice || '',
    reorderLevel: product?.reorderLevel || '',
    categoryId: product?.category?.id || '',
    supplierId: product?.supplier?.id || '',
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [suppliers, setSuppliers] = useState([]);

  useEffect(() => {
    const fetchDropdownData = async () => {
      try {
        const [categoriesRes, suppliersRes] = await Promise.all([
          categoriesApi.getAll(),
          suppliersApi.getAll()
        ]);
        setCategories(categoriesRes.data);
        setSuppliers(suppliersRes.data);
      } catch (error) {
        console.error('Error fetching categories and suppliers for form:', error);
      }
    };
    fetchDropdownData();
  }, []);

  const validateForm = () => {
    const newErrors = {};
    if (!formData.name.trim()) newErrors.name = 'Product name is required';
    if (!formData.sku.trim()) newErrors.sku = 'SKU is required';
    if (!formData.price || parseFloat(formData.price) <= 0) newErrors.price = 'Valid price is required';
    if (!formData.categoryId) newErrors.categoryId = 'Category is required';
    if (!formData.supplierId) newErrors.supplierId = 'Supplier is required';
    return newErrors;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = validateForm();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setLoading(true);
    try {
      const payload = {
        sku: formData.sku,
        name: formData.name,
        description: formData.description,
        unitPrice: Number(formData.price),
        reorderLevel: Number(formData.reorderLevel || 0),
        category: { id: Number(formData.categoryId) },
        supplier: { id: Number(formData.supplierId) },
        stockQuantity: product?.stockQuantity || 0,
        isActive: product?.isActive !== undefined ? product.isActive : true,
      };
      await onSubmit(payload);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="form-container">
      <h3 className="form-title">{product?.id ? 'Edit Product' : 'New Product'}</h3>

      <div className="form-grid-2">
        <div className="form-group">
          <label className="form-label required">Product Name</label>
          <input
            type="text"
            name="name"
            className="form-input"
            value={formData.name}
            onChange={handleChange}
            placeholder="Enter product name"
          />
          {errors.name && <span className="form-error">{errors.name}</span>}
        </div>

        <div className="form-group">
          <label className="form-label required">SKU</label>
          <input
            type="text"
            name="sku"
            className="form-input"
            value={formData.sku}
            onChange={handleChange}
            placeholder="Enter SKU"
          />
          {errors.sku && <span className="form-error">{errors.sku}</span>}
        </div>
      </div>

      <div className="form-group">
        <label className="form-label">Description</label>
        <textarea
          name="description"
          className="form-textarea"
          value={formData.description}
          onChange={handleChange}
          placeholder="Enter product description"
        />
      </div>

      <div className="form-grid-2">
        <div className="form-group">
          <label className="form-label required">Category</label>
          <select
            name="categoryId"
            className="form-select"
            value={formData.categoryId}
            onChange={handleChange}
          >
            <option value="">Select Category</option>
            {categories.map(cat => (
              <option key={cat.id} value={cat.id}>{cat.name}</option>
            ))}
          </select>
          {errors.categoryId && <span className="form-error">{errors.categoryId}</span>}
        </div>

        <div className="form-group">
          <label className="form-label required">Supplier</label>
          <select
            name="supplierId"
            className="form-select"
            value={formData.supplierId}
            onChange={handleChange}
          >
            <option value="">Select Supplier</option>
            {suppliers.map(sup => (
              <option key={sup.id} value={sup.id}>{sup.name}</option>
            ))}
          </select>
          {errors.supplierId && <span className="form-error">{errors.supplierId}</span>}
        </div>
      </div>

      <div className="form-grid-2">
        <div className="form-group">
          <label className="form-label required">Price</label>
          <input
            type="number"
            name="price"
            className="form-input"
            value={formData.price}
            onChange={handleChange}
            placeholder="0.00"
            step="0.01"
            min="0"
          />
          {errors.price && <span className="form-error">{errors.price}</span>}
        </div>

        <div className="form-group">
          <label className="form-label">Reorder Level</label>
          <input
            type="number"
            name="reorderLevel"
            className="form-input"
            value={formData.reorderLevel}
            onChange={handleChange}
            placeholder="0"
            min="0"
          />
        </div>
      </div>

      <div className="form-actions">
        <button type="button" className="btn-cancel" onClick={onCancel}>Cancel</button>
        <button type="submit" className="btn-submit" disabled={loading}>
          {loading ? 'Saving...' : 'Save Product'}
        </button>
      </div>
    </form>
  );
};

export default ProductForm;

