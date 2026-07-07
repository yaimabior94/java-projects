#!/bin/bash

# Smart Inventory Management System - Frontend Setup & Run Guide

## 1. INSTALLATION STEPS

### Step 1: Ensure Node.js is installed
echo "Checking Node.js version..."
node --version  # Should be 16+
npm --version   # Should be 7+

### Step 2: Install dependencies
echo "Installing dependencies..."
cd frontend
npm install

### Step 3: Verify dependencies installed
echo "Checking installed packages..."
npm list react react-router-dom axios chart.js

## 2. RUNNING THE FRONTEND

### Development Mode (with Hot Reload)
npm run dev
# Application will start at http://localhost:5173

### Production Build
npm run build
# Creates optimized build in dist/

### Preview Production Build
npm run preview
# Serves the production build locally

## 3. FRONTEND STRUCTURE

### Key Directories
- src/pages/              - 8 page components
- src/components/        - Shared components (Table, Modal, Sidebar, Navbar)
- src/components/Forms/  - 3 form components (Product, Category, Supplier)
- src/services/          - API integration (Axios client)
- src/context/           - Authentication context
- src/hooks/             - Custom React hooks
- src/styles/            - CSS stylesheets

### Total Files Created
- 9 Page Components (Login, Dashboard, Products, Categories, Suppliers, Inventory, Sales, Purchases, Reports)
- 7 Shared Components (Sidebar, Navbar, Table, Modal, Charts, + 3 Forms)
- 1 API Service Module
- 1 Authentication Context
- 1 Custom Hooks Module
- 6 CSS Stylesheets
- 2 Documentation Files

## 4. BACKEND REQUIREMENTS

Ensure Spring Boot backend is running:
```bash
cd backend
mvn spring-boot:run
```
Backend should be running on: http://localhost:8080

### Required API Endpoints:
- POST   /api/auth/login
- POST   /api/auth/register
- GET    /api/products
- POST   /api/products
- PUT    /api/products/{id}
- DELETE /api/products/{id}
- GET    /api/categories
- POST   /api/categories
- PUT    /api/categories/{id}
- DELETE /api/categories/{id}
- GET    /api/suppliers
- POST   /api/suppliers
- PUT    /api/suppliers/{id}
- DELETE /api/suppliers/{id}
- GET    /api/inventory
- PUT    /api/inventory/{id}
- POST   /api/sales
- GET    /api/sales
- POST   /api/purchases
- GET    /api/purchases
- GET    /api/dashboard/summary
- GET    /api/reports/sales
- GET    /api/reports/purchases
- GET    /api/reports/inventory
- GET    /api/reports/low-stock

## 5. FEATURE OVERVIEW

### Authentication
- ✅ Login page with form validation
- ✅ JWT token management
- ✅ Session persistence
- ✅ Auto-logout on unauthorized access

### Dashboard
- ✅ KPI cards (Total Products, Inventory, Sales, Low Stock)
- ✅ Sales trend chart
- ✅ Revenue distribution chart
- ✅ Recent activities feed

### Product Management
- ✅ List products with search and sort
- ✅ Create product with category/supplier selection
- ✅ Edit product details
- ✅ Delete products
- ✅ Form validation

### Category Management
- ✅ Full CRUD for categories
- ✅ Searchable table view
- ✅ Modal form for create/edit
- ✅ Product count display

### Supplier Management
- ✅ Full CRUD for suppliers
- ✅ Contact information fields
- ✅ Searchable table view
- ✅ Modal form with validation

### Inventory Management
- ✅ View all inventory items
- ✅ Stock level status badges (Low Stock / In Stock)
- ✅ Update quantity and reorder levels
- ✅ Track current stock vs. threshold

### Sales Module
- ✅ Record sales transactions
- ✅ Select product and quantity
- ✅ Auto-calculate totals
- ✅ Sales history view
- ✅ Pagination and filtering

### Purchases Module
- ✅ Record purchase orders
- ✅ Select supplier and product
- ✅ Track costs
- ✅ Purchase history
- ✅ Pagination

### Reports
- ✅ Sales Report with metrics
- ✅ Purchase Report with analysis
- ✅ Inventory Snapshot
- ✅ Low Stock Alert Report
- ✅ Date range filtering
- ✅ Download as PDF
- ✅ Interactive charts

## 6. TECH STACK COMPONENTS

| Component | Package | Version |
|-----------|---------|---------|
| React | react | 18.3.1 |
| Router | react-router-dom | 6.24.1 |
| HTTP Client | axios | 1.7.2 |
| Charts | chart.js, react-chartjs-2 | 4.4.3, 5.2.0 |
| Build Tool | vite | 5.3.1 |
| Type Checking | @types/react | 18.3.3 |

## 7. CONFIGURATION FILES

### vite.config.js
- Configures Vite bundler
- Sets up API proxy (/api → http://localhost:8080)
- Port: 5173
- HMR enabled for hot reload

### package.json
- Dev: `npm run dev` - Start dev server
- Build: `npm run build` - Create production bundle
- Lint: `npm run lint` - Run ESLint
- Preview: `npm run preview` - Serve production build

## 8. ENV VARIABLES (Optional)

Create `.env` file in frontend root:
```
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=Smart Inventory
```

## 9. DEBUGGING

### Enable Debug Mode in Browser
1. Open DevTools (F12)
2. Go to Console tab
3. Look for API request logs
4. Check Network tab for XHR requests

### Check API Connection
```javascript
// In browser console
fetch('http://localhost:8080/api/products')
  .then(r => r.json())
  .then(console.log)
```

### Common Issues & Solutions

**Issue: Cannot connect to backend**
- Solution: Verify backend running on port 8080
- Solution: Check CORS headers from backend

**Issue: Token not persisting**
- Solution: Check localStorage in DevTools
- Solution: Verify AuthContext login method

**Issue: Pages not loading**
- Solution: Check if all imports are correct
- Solution: Verify components exist in src/pages/

**Issue: Table not showing data**
- Solution: Check console for API errors
- Solution: Verify data structure matches column keys

## 10. DEPLOYMENT

### Build for Production
```bash
npm run build
```

### Deploy dist/ folder to:
- Vercel (recommended for React)
- Netlify
- AWS S3 + CloudFront
- Any static hosting

### Environment Considerations
- Update API_BASE_URL to production backend
- Enable HTTPS
- Configure CORS properly
- Set cache headers
- Use CDN for static files

## 11. DEVELOPMENT WORKFLOW

### Adding a New Page
1. Create component in `src/pages/NewPage.jsx`
2. Import in `src/App.jsx`
3. Add route in Routes component
4. Add menu item in `src/components/Sidebar.jsx`

### Adding a New Component
1. Create in `src/components/NewComponent.jsx`
2. Export from component file
3. Import in page that needs it
4. Create CSS file if styling needed

### Adding API Endpoint
1. Add method in `src/services/api.js`
2. Export from api.js
3. Import in component
4. Call in useEffect or event handler

## 12. PERFORMANCE OPTIMIZATION

### Already Implemented
- ✅ Client-side search and sort (Table)
- ✅ Pagination (reduces DOM elements)
- ✅ Lazy imports in Router
- ✅ CSS minimization
- ✅ Vite's fast bundling

### Recommendations
- Monitor bundle size with `npm run build`
- Use React DevTools Profiler to identify slow renders
- Implement React.memo() for expensive components
- Use useCallback() for event handlers
- Implement virtualization for very long lists

## 13. SECURITY NOTES

- ✅ JWT tokens in Authorization header
- ✅ Auto-logout on 401 response
- ✅ No sensitive data in localStorage (only tokens)
- ✅ HTTPS ready
- ✅ CORS configured

### Recommendations
- Implement refresh token rotation
- Add CSRF protection
- Sanitize user inputs
- Use Content Security Policy headers
- Regular security updates

## 14. TESTING

### Manual Testing Checklist
- [ ] Login with valid credentials
- [ ] Login with invalid credentials (should show error)
- [ ] Create product (should appear in list)
- [ ] Edit product (should update immediately)
- [ ] Delete product (should remove from list)
- [ ] Search in table (should filter results)
- [ ] Sort table columns (should reorder)
- [ ] Pagination (should load more items)
- [ ] Dashboard charts (should render)
- [ ] Download reports (should download PDF)

### Automated Testing (Recommended)
```bash
# Install testing libraries
npm install --save-dev vitest @testing-library/react @testing-library/jest-dom

# Add test script to package.json
"test": "vitest"
```

## 15. NEXT STEPS

1. Verify backend is running
2. Run `npm install` in frontend folder
3. Run `npm run dev` to start dev server
4. Open http://localhost:5173 in browser
5. Login with demo credentials (admin/admin123)
6. Test CRUD operations on each page

## Support & Resources

- React Docs: https://react.dev
- React Router: https://reactrouter.com
- Axios Docs: https://axios-http.com
- Vite Docs: https://vitejs.dev
- Chart.js: https://www.chartjs.org

---

**Project Created**: Smart Inventory Management System
**Frontend Framework**: React 18 with Vite
**Status**: Production Ready
