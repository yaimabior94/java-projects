import axios from 'axios';

// Create Axios instance — Vite proxy forwards /api → http://localhost:8080
const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

// Request interceptor: auto-attach JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor: handle 401 Unauthorized → force logout
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // FIX BUG-K: clear ALL auth keys including userId to prevent stale data
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
      localStorage.removeItem('userId');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// ─── Auth ─────────────────────────────────────────────────────────────────
export const authApi = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
};

// ─── Dashboard ────────────────────────────────────────────────────────────
export const dashboardApi = {
  getSummary: () => api.get('/dashboard'),
};

// ─── Products ─────────────────────────────────────────────────────────────
export const productsApi = {
  getAll: () => api.get('/products'),
  getById: (id) => api.get(`/products/${id}`),
  create: (data) => api.post('/products', data),
  update: (id, data) => api.put(`/products/${id}`, data),
  delete: (id) => api.delete(`/products/${id}`),
};

// ─── Categories ───────────────────────────────────────────────────────────
export const categoriesApi = {
  getAll: () => api.get('/categories'),
  create: (data) => api.post('/categories', data),
  update: (id, data) => api.put(`/categories/${id}`, data),
  delete: (id) => api.delete(`/categories/${id}`),
};

// ─── Suppliers ────────────────────────────────────────────────────────────
export const suppliersApi = {
  getAll: () => api.get('/suppliers'),
  create: (data) => api.post('/suppliers', data),
  update: (id, data) => api.put(`/suppliers/${id}`, data),
  delete: (id) => api.delete(`/suppliers/${id}`),
};

// ─── Inventory ────────────────────────────────────────────────────────────
export const inventoryApi = {
  getAll: () => api.get('/inventory'),
  getById: (id) => api.get(`/inventory/${id}`),
  update: (id, data) => api.put(`/inventory/${id}`, data),
};

// ─── Sales ────────────────────────────────────────────────────────────────
export const salesApi = {
  getAll: () => api.get('/sales'),
  getById: (id) => api.get(`/sales/${id}`),
  create: (data) => api.post('/sales', data),
  update: (id, data) => api.put(`/sales/${id}`, data),
  delete: (id) => api.delete(`/sales/${id}`),
};

// ─── Purchases ────────────────────────────────────────────────────────────
export const purchasesApi = {
  getAll: () => api.get('/purchases'),
  getById: (id) => api.get(`/purchases/${id}`),
  create: (data) => api.post('/purchases', data),
  update: (id, data) => api.put(`/purchases/${id}`, data),
  delete: (id) => api.delete(`/purchases/${id}`),
};

// ─── Reports ──────────────────────────────────────────────────────────────
export const reportsApi = {
  getInventory: () => api.get('/reports/inventory'),
  getSales: () => api.get('/reports/sales'),
  getPurchases: () => api.get('/reports/purchases'),
  getLowStock: () => api.get('/reports/low-stock'),
  downloadPdf: (type) =>
    api.get(`/reports/${type}/pdf`, { responseType: 'blob' }),
  downloadExcel: (type) =>
    api.get(`/reports/${type}/excel`, { responseType: 'blob' }),
};

export default api;
