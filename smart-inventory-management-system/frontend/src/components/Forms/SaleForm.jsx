import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import '../../styles/Forms.css';

const SaleForm = ({ sale, products = [], onSubmit, onCancel }) => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    invoiceNumber: sale?.invoiceNumber || `INV-${Date.now()}`,
    customerName: sale?.customerName || '',
    discount: sale?.discount || '0',
    tax: sale?.tax || '0',
    paymentStatus: sale?.paymentStatus || 'PENDING',
  });
  const [items, setItems] = useState(
    sale?.saleItems || [{ productId: '', quantity: '1', unitPrice: '' }]
  );
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors(prev => ({ ...prev, [name]: '' }));
  };

  const handleItemChange = (index, field, value) => {
    const updated = [...items];
    updated[index] = { ...updated[index], [field]: value };
    if (field === 'productId' && value) {
      const product = products.find(p => p.id === Number(value));
      if (product) updated[index].unitPrice = product.unitPrice;
    }
    setItems(updated);
  };

  const addItem = () => setItems([...items, { productId: '', quantity: '1', unitPrice: '' }]);

  const removeItem = (index) => {
    if (items.length > 1) setItems(items.filter((_, i) => i !== index));
  };

  const calculateTotal = () => {
    const subtotal = items.reduce((sum, item) => {
      return sum + (Number(item.quantity) || 0) * (Number(item.unitPrice) || 0);
    }, 0);
    return subtotal - Number(formData.discount || 0) + Number(formData.tax || 0);
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.customerName.trim()) newErrors.customerName = 'Customer name is required';
    const validItems = items.filter(i => i.productId && Number(i.quantity) > 0);
    if (validItems.length === 0) newErrors.items = 'At least one item is required';
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = validateForm();
    if (Object.keys(newErrors).length > 0) { setErrors(newErrors); return; }

    setLoading(true);
    try {
      const saleItems = items
        .filter(i => i.productId && Number(i.quantity) > 0)
        .map(item => ({
          product: { id: Number(item.productId) },
          quantity: Number(item.quantity),
          unitPrice: Number(item.unitPrice),
          lineTotal: Number(item.quantity) * Number(item.unitPrice),
        }));

      const payload = {
        invoiceNumber: formData.invoiceNumber,
        customerName: formData.customerName,
        totalAmount: calculateTotal(),
        discount: Number(formData.discount || 0),
        tax: Number(formData.tax || 0),
        paymentStatus: formData.paymentStatus,
        // FIX BUG-M: use explicit null check instead of || 1
        // If userId is 0 (falsy), || 1 would silently assign the wrong user.
        createdBy: { id: user?.userId != null ? user.userId : 1 },
        saleItems,
      };
      await onSubmit(payload);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="form-container">
      <h3 className="form-title">{sale?.id ? 'Edit Sale' : 'New Sale'}</h3>

      <div className="form-grid-2">
        <div className="form-group">
          <label className="form-label">Invoice #</label>
          <input type="text" name="invoiceNumber" className="form-input" value={formData.invoiceNumber} onChange={handleChange} readOnly />
        </div>
        <div className="form-group">
          <label className="form-label required">Customer Name</label>
          <input type="text" name="customerName" className="form-input" value={formData.customerName} onChange={handleChange} placeholder="Customer name" />
          {errors.customerName && <span className="form-error">{errors.customerName}</span>}
        </div>
      </div>

      <div style={{ marginBottom: '16px' }}>
        <label className="form-label required" style={{ marginBottom: '8px', display: 'block' }}>Sale Items</label>
        {errors.items && <span className="form-error" style={{ display: 'block', marginBottom: '8px' }}>{errors.items}</span>}
        {items.map((item, idx) => (
          <div key={idx} style={{ display: 'flex', gap: '8px', marginBottom: '8px', alignItems: 'center' }}>
            <div style={{ flex: 2, minWidth: 0 }}>
              <select className="form-input" value={item.productId} onChange={e => handleItemChange(idx, 'productId', e.target.value)}>
                <option value="">Select Product</option>
                {products.map(p => <option key={p.id} value={p.id}>{p.name} (Stock: {p.stockQuantity})</option>)}
              </select>
            </div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <input type="number" className="form-input" value={item.quantity} onChange={e => handleItemChange(idx, 'quantity', e.target.value)} placeholder="Qty" min="1" />
            </div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <input type="number" className="form-input" value={item.unitPrice} onChange={e => handleItemChange(idx, 'unitPrice', e.target.value)} placeholder="Price" step="0.01" readOnly />
            </div>
            <div style={{ flex: 1, color: '#9ca3af', fontSize: '0.9rem', textAlign: 'right', whiteSpace: 'nowrap' }}>
              ${((Number(item.quantity) || 0) * (Number(item.unitPrice) || 0)).toFixed(2)}
            </div>
            {items.length > 1 && (
              <button type="button" onClick={() => removeItem(idx)} style={{ background: '#ef4444', color: '#fff', border: 'none', borderRadius: '4px', padding: '10px 12px', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>✕</button>
            )}
          </div>
        ))}
        <button type="button" onClick={addItem} style={{ background: 'rgba(59,130,246,0.2)', color: '#3b82f6', border: '1px solid rgba(59,130,246,0.3)', borderRadius: '6px', padding: '6px 14px', cursor: 'pointer', fontSize: '0.85rem' }}>+ Add Item</button>
      </div>

      <div className="form-grid-2">
        <div className="form-group">
          <label className="form-label">Discount ($)</label>
          <input type="number" name="discount" className="form-input" value={formData.discount} onChange={handleChange} step="0.01" min="0" />
        </div>
        <div className="form-group">
          <label className="form-label">Tax ($)</label>
          <input type="number" name="tax" className="form-input" value={formData.tax} onChange={handleChange} step="0.01" min="0" />
        </div>
      </div>

      <div className="form-grid-2">
        <div className="form-group">
          <label className="form-label">Payment Status</label>
          <select name="paymentStatus" className="form-input" value={formData.paymentStatus} onChange={handleChange}>
            <option value="PENDING">Pending</option>
            <option value="PAID">Paid</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </div>
        <div className="form-group">
          <label className="form-label">Total</label>
          <input type="text" className="form-input" value={`$${calculateTotal().toFixed(2)}`} readOnly style={{ fontWeight: 'bold', color: '#10b981' }} />
        </div>
      </div>

      <div className="form-actions">
        <button type="button" className="btn-cancel" onClick={onCancel}>Cancel</button>
        <button type="submit" className="btn-submit" disabled={loading}>
          {loading ? 'Saving...' : 'Save Sale'}
        </button>
      </div>
    </form>
  );
};

export default SaleForm;
