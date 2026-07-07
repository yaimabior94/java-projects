# 🚀 React Frontend - Quick Reference

## INSTANT START

```bash
# 1. Install
cd frontend
npm install

# 2. Run
npm run dev

# 3. Open
http://localhost:5173

# 4. Login
Username: admin
Password: admin123
```

---

## FILE TREE

```
frontend/
├── src/
│   ├── pages/
│   │   ├── Login.jsx ✅
│   │   ├── Dashboard.jsx ✅
│   │   ├── Products.jsx ✅
│   │   ├── Categories.jsx ✅
│   │   ├── Suppliers.jsx ✅
│   │   ├── Inventory.jsx ✅
│   │   ├── Sales.jsx ✅
│   │   ├── Purchases.jsx ✅
│   │   └── Reports.jsx ✅
│   │
│   ├── components/
│   │   ├── Sidebar.jsx ✅
│   │   ├── Navbar.jsx ✅
│   │   ├── Table.jsx ✅
│   │   ├── Modal.jsx ✅
│   │   ├── Charts.jsx ✅
│   │   └── Forms/
│   │       ├── ProductForm.jsx ✅
│   │       ├── CategoryForm.jsx ✅
│   │       └── SupplierForm.jsx ✅
│   │
│   ├── services/
│   │   └── api.js ✅ (Axios + Interceptors)
│   │
│   ├── context/
│   │   └── AuthContext.jsx ✅
│   │
│   ├── hooks/
│   │   └── useCustom.js ✅
│   │
│   ├── styles/
│   │   ├── Sidebar.css ✅
│   │   ├── Navbar.css ✅
│   │   ├── Table.css ✅
│   │   ├── Modal.css ✅
│   │   ├── Forms.css ✅
│   │   └── Login.css ✅
│   │
│   ├── App.jsx ✅
│   ├── main.jsx ✅
│   └── index.css ✅
│
├── vite.config.js ✅
├── package.json ✅
└── index.html ✅
```

---

## NPM COMMANDS

```bash
npm run dev       # Start dev server (port 5173)
npm run build     # Production build
npm run preview   # Preview production build
npm run lint      # Run linter
npm install       # Install dependencies
npm update        # Update packages
```

---

## API ENDPOINTS USED

```
POST   /api/auth/login
GET    /api/products
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
GET    /api/categories
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}
GET    /api/suppliers
POST   /api/suppliers
PUT    /api/suppliers/{id}
DELETE /api/suppliers/{id}
GET    /api/inventory
PUT    /api/inventory/{id}
POST   /api/sales
GET    /api/sales
POST   /api/purchases
GET    /api/purchases
GET    /api/dashboard/summary
GET    /api/reports/sales
GET    /api/reports/purchases
GET    /api/reports/inventory
GET    /api/reports/low-stock
```

---

## COMPONENT PROPS

### Table
```jsx
<Table
  columns={[
    { key: 'name', label: 'Name' },
    { key: 'price', label: 'Price' }
  ]}
  data={items}
  onEdit={(item) => {}}
  onDelete={(item) => {}}
  pageSize={10}
/>
```

### Modal
```jsx
<Modal
  isOpen={true}
  onClose={() => {}}
  title="Title"
  size="md"  // sm, md, lg
>
  Content here
</Modal>
```

### Chart Components
```jsx
import { SalesChart, RevenueChart, InventoryChart, CategoryDistribution } from './components/Charts'

<SalesChart data={{ labels: [...], values: [...] }} />
<RevenueChart data={{ labels: [...], values: [...] }} />
<InventoryChart data={{ labels: [...], values: [...] }} />
<CategoryDistribution data={{ labels: [...], values: [...] }} />
```

---

## CONTEXT USAGE

```jsx
import { useAuth } from './context/AuthContext'

const { user, loading, login, logout } = useAuth()

// user structure:
{
  token: 'jwt_token',
  username: 'admin',
  role: 'ADMIN'
}
```

---

## HOOKS USAGE

```jsx
// useForm
const { values, handleChange, handleSubmit, errors } = useForm(
  { name: '', email: '' },
  async (data) => { /* submit */ }
)

// useAsync
const { execute, status, value, error } = useAsync(apiCall)

// useLocalStorage
const [theme, setTheme] = useLocalStorage('theme', 'dark')
```

---

## API SERVICE USAGE

```jsx
import { 
  productsApi, 
  categoriesApi, 
  authApi, 
  dashboardApi 
} from '../services/api'

// All return promises
await productsApi.getAll()
await productsApi.create(data)
await productsApi.update(id, data)
await productsApi.delete(id)
```

---

## ROUTES

```jsx
/login              → Login page
/                   → Dashboard
/products           → Products CRUD
/categories         → Categories CRUD
/suppliers          → Suppliers CRUD
/inventory          → Inventory tracking
/sales              → Sales transactions
/purchases          → Purchase orders
/reports            → Analytics & reports
```

---

## PAGES FEATURES

| Page | Features |
|------|----------|
| Login | Form, validation, error handling |
| Dashboard | KPIs, charts, activities |
| Products | Search, sort, pagination, CRUD |
| Categories | Search, sort, pagination, CRUD |
| Suppliers | Search, sort, pagination, CRUD |
| Inventory | Status badges, stock updates |
| Sales | Transaction recording, history |
| Purchases | Order recording, history |
| Reports | 4 report types, PDF, charts |

---

## STYLING

### CSS Variables
```css
--primary: #3b82f6
--secondary: #10b981
--danger: #ef4444
--warning: #f59e0b
--bg-primary: #0f172a
--bg-secondary: #1e293b
--text-primary: #ffffff
--text-secondary: #cbd5e1
```

### Common Classes
```css
.btn-primary       /* Blue button */
.btn-secondary     /* Gray button */
.btn-danger        /* Red button */
.card              /* Card container */
.badge             /* Status badge */
.error             /* Error message */
.loading           /* Loading state */
.grid-2            /* 2-column grid */
```

---

## DEPENDENCIES

```json
{
  "axios": "^1.7.2",
  "chart.js": "^4.4.3",
  "react": "^18.3.1",
  "react-chartjs-2": "^5.2.0",
  "react-dom": "^18.3.1",
  "react-router-dom": "^6.24.1"
}
```

---

## ENV VARIABLES (Optional)

```
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=Smart Inventory
```

---

## TROUBLESHOOTING

| Issue | Solution |
|-------|----------|
| Cannot connect to API | Check backend on 8080 |
| 401 errors | Verify JWT token in localStorage |
| Page not found | Check route in App.jsx |
| Form not submitting | Check validation errors |
| Charts not showing | Verify data structure |
| Modal not closing | Check onClose handler |

---

## PRODUCTION BUILD

```bash
# Build
npm run build

# Output: dist/ folder
# Deploy: dist/ contents to server
# Configure: Backend API URL
# Test: All features before launch
```

---

## DOCUMENTATION

📖 [README.md](./README.md) - Full documentation  
🗂️ [PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md) - Architecture  
📝 [FRONTEND_SETUP_GUIDE.md](../FRONTEND_SETUP_GUIDE.md) - Setup guide  
📋 [CREATED_FILES_INVENTORY.md](../CREATED_FILES_INVENTORY.md) - File listing  
✅ [FRONTEND_COMPLETE.md](../FRONTEND_COMPLETE.md) - Completion status  

---

## KEY STATS

- 📄 **9 Pages** - Full-featured pages
- 🧩 **7 Shared Components** - Reusable modules
- 📝 **3 Form Components** - CRUD forms
- 🎨 **6 Style Files** - Responsive CSS
- 🔧 **1 API Service** - Centralized Axios
- 🔐 **1 Auth Context** - State management
- 🪝 **3 Custom Hooks** - Utilities
- 📚 **4 Documentation Files** - Complete guides

---

## SUCCESS CHECKLIST

- ✅ All pages created
- ✅ Components built
- ✅ API service ready
- ✅ Authentication working
- ✅ Styling complete
- ✅ Documentation written
- ✅ Production ready

---

**Status: READY TO DEPLOY 🚀**

Start development now: `npm run dev`
