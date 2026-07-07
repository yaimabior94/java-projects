# Frontend Project Structure

## Quick Reference

### Pages (8 total)
1. **Login** - Authentication entry point
2. **Dashboard** - KPIs, charts, recent activities
3. **Products** - Product CRUD management
4. **Categories** - Category management
5. **Suppliers** - Supplier contact management
6. **Inventory** - Stock level management
7. **Sales** - Sale transaction recording
8. **Purchases** - Purchase order management
9. **Reports** - Analytics and reporting

### Shared Components
- **Sidebar.jsx** - Collapsible navigation
- **Navbar.jsx** - Top bar with user menu
- **Table.jsx** - Reusable data table with search, sort, pagination
- **Modal.jsx** - Reusable dialog component
- **Charts.jsx** - Chart components (Line, Bar, Pie, Doughnut)

### Form Components
- **ProductForm.jsx** - Product creation/editing
- **CategoryForm.jsx** - Category creation/editing
- **SupplierForm.jsx** - Supplier creation/editing

### Services
- **api.js** - Axios client with:
  - Base URL and headers configuration
  - Request interceptor (adds JWT token)
  - Response interceptor (handles 401 errors)
  - Grouped API methods by resource

### Context
- **AuthContext.jsx** - Authentication state:
  - user (token, username, role)
  - login/logout functions
  - loading state

### Hooks
- **useCustom.js** - Custom hooks:
  - useForm - Form state and validation
  - useAsync - Async operations
  - useLocalStorage - Persistent storage

### Styles
Global CSS:
- Color variables (primary, secondary, danger, warning)
- Dark theme (bg-primary, bg-secondary, bg-tertiary)
- Typography and spacing utilities
- Component-specific CSS files

## Component Relationships

```
App.jsx
├── Router & AuthProvider
├── Login Page (public route)
└── AppLayout (protected)
    ├── Sidebar
    ├── Navbar
    └── Main Content (Routes)
        ├── Dashboard
        ├── Products → ProductForm
        ├── Categories → CategoryForm
        ├── Suppliers → SupplierForm
        ├── Inventory → Update Modal
        ├── Sales → Create Modal
        ├── Purchases → Create Modal
        └── Reports

Shared Components Used by Multiple Pages:
- Table (Products, Categories, Suppliers, Inventory, Sales, Purchases, Reports)
- Modal (all pages with create/edit functionality)
- Charts (Dashboard, Reports)
- Forms (Products, Categories, Suppliers)
```

## API Flow

```
Component State Update
         ↓
Form Submission / User Action
         ↓
API Service Call (api.js)
         ↓
Request Interceptor (adds JWT)
         ↓
Backend API (Spring Boot)
         ↓
Response
         ↓
Response Interceptor (check 401)
         ↓
Component State Update
         ↓
UI Re-render
```

## Data Flow Example: Create Product

1. User clicks "New Product" button
2. Modal opens with ProductForm
3. User fills form and submits
4. ProductForm calls `onSubmit(formData)`
5. Products page calls `productsApi.create(formData)`
6. API service sends POST to `/api/products`
7. Request interceptor adds JWT token
8. Backend processes and returns new product
9. Products page calls `fetchProducts()`
10. Table updates with new data

## Key Implementation Details

### Authentication
- Credentials stored in localStorage
- JWT token sent in Authorization header
- Auto-logout on 401 response
- Session restored on app reload

### Forms
- Controlled components (state-driven)
- Validation before submit
- Error messages below fields
- Loading state on submit button

### Tables
- Client-side search across all columns
- Click column headers to sort
- Pagination with customizable page size
- Edit/Delete buttons in last column
- Empty state message when no data

### Modals
- Click outside or press ESC to close
- Prevents body scroll when open
- Smooth fade/slide animations
- Content scrollable if too long

### Error Handling
- Try-catch on all async operations
- User-friendly error messages
- Error alerts auto-dismiss after 4 seconds
- Console logging for debugging

## Build & Deployment

### Development
```bash
npm run dev
```

### Production Build
```bash
npm run build
```

### Preview Build
```bash
npm run preview
```

### Lint Check
```bash
npm run lint
```

## Environment Setup

### Prerequisites
- Node.js 16+
- npm or yarn

### Vite Config
- Proxy: `/api/*` → `http://localhost:8080`
- Port: 5173
- HMR: Hot Module Replacement enabled

## Performance Metrics

- Initial load: ~2-3 seconds
- Route transitions: < 500ms
- API response time: Backend dependent
- Table pagination: Smooth with 15+ rows

## Known Limitations & Future Improvements

- [ ] Add role-based page visibility
- [ ] Add bulk import/export for products
- [ ] Add inventory history/audit log
- [ ] Add email notifications for low stock
- [ ] Add dark/light theme toggle
- [ ] Add PWA offline support
- [ ] Add advanced filtering options
- [ ] Add data export to Excel
- [ ] Add real-time inventory sync
- [ ] Add user profile page

## Deployment Checklist

- [ ] Update API base URL in production
- [ ] Enable HTTPS
- [ ] Set proper CORS headers
- [ ] Configure environment variables
- [ ] Minimize bundle size
- [ ] Set cache headers
- [ ] Add loading indicators
- [ ] Test all CRUD operations
- [ ] Verify error handling
- [ ] Security audit (no secrets in code)
