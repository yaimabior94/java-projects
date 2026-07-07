# Smart Inventory Management System - Frontend

## Project Overview

A modern React-based frontend for the Smart Inventory Management System, built with:
- **React 18.3** - Modern UI library
- **React Router 6** - Client-side routing
- **Axios** - HTTP client for API communication
- **Chart.js & react-chartjs-2** - Data visualization
- **Vite** - Ultra-fast build tool

## Folder Structure

```
frontend/
├── src/
│   ├── components/          # Reusable React components
│   │   ├── Forms/
│   │   │   ├── ProductForm.jsx
│   │   │   ├── CategoryForm.jsx
│   │   │   └── SupplierForm.jsx
│   │   ├── Sidebar.jsx      # Navigation sidebar
│   │   ├── Navbar.jsx       # Top navigation bar
│   │   ├── Table.jsx        # Reusable data table
│   │   ├── Modal.jsx        # Reusable modal dialog
│   │   └── Charts.jsx       # Chart components (Line, Bar, Pie, Doughnut)
│   │
│   ├── pages/               # Page components (one per route)
│   │   ├── Login.jsx        # Authentication page
│   │   ├── Dashboard.jsx    # Main dashboard with KPIs and charts
│   │   ├── Products.jsx     # Product management CRUD
│   │   ├── Categories.jsx   # Category management CRUD
│   │   ├── Suppliers.jsx    # Supplier management CRUD
│   │   ├── Inventory.jsx    # Inventory tracking & updates
│   │   ├── Sales.jsx        # Sales recording
│   │   ├── Purchases.jsx    # Purchase orders
│   │   └── Reports.jsx      # Analytics & reports
│   │
│   ├── context/
│   │   └── AuthContext.jsx  # Authentication state management
│   │
│   ├── hooks/
│   │   └── useCustom.js     # Custom hooks (useForm, useAsync, useLocalStorage)
│   │
│   ├── services/
│   │   └── api.js           # Axios API client with interceptors
│   │
│   ├── styles/              # CSS stylesheets
│   │   ├── Sidebar.css
│   │   ├── Navbar.css
│   │   ├── Table.css
│   │   ├── Modal.css
│   │   ├── Forms.css
│   │   └── Login.css
│   │
│   ├── App.jsx              # Main app component with routing
│   ├── main.jsx             # React entry point
│   └── index.css            # Global styles
│
├── index.html               # HTML template
├── vite.config.js           # Vite configuration with API proxy
├── package.json
└── README.md
```

## Installation & Setup

### Prerequisites
- Node.js 16+ and npm

### Install Dependencies
```bash
npm install
```

### Environment Variables
Create a `.env` file in the frontend root (optional):
```
VITE_API_BASE_URL=http://localhost:8080/api
```

### Development Server
```bash
npm run dev
```
The app will be available at `http://localhost:5173`

### Build for Production
```bash
npm run build
```

## Key Features

### 1. **Authentication**
- Login page with credentials validation
- JWT token-based authentication
- Automatic logout on unauthorized access (401)
- Session persistence via localStorage

### 2. **Dashboard**
- KPI cards showing:
  - Total Products
  - Total Inventory
  - Total Sales
  - Low Stock Count
- Sales trend chart (line chart)
- Revenue by category chart (bar chart)
- Recent activities feed

### 3. **Product Management**
- List all products in searchable/sortable table
- Create new products with form validation
- Edit product details
- Delete products
- Displays: Name, SKU, Category, Price, Stock

### 4. **Category Management**
- CRUD operations for product categories
- List view with search and sort
- Modal form for create/edit
- Product count per category

### 5. **Supplier Management**
- Supplier CRUD with contact information
- Fields: Name, Email, Phone, Address, City, Country
- Searchable/sortable table
- Modal form with validation

### 6. **Inventory Management**
- Real-time inventory view
- Stock level indicators (In Stock / Low Stock badges)
- Update quantity and reorder levels
- Track current stock vs. reorder threshold

### 7. **Sales Module**
- Record sales transactions
- Select product and quantity
- Automatic total calculation
- Sales history with dates
- Filters and search capabilities

### 8. **Purchases Module**
- Record purchase orders
- Select supplier and product
- Track unit cost and total amount
- Purchase history and tracking
- Date range filtering

### 9. **Reports**
- Multiple report types:
  - **Sales Report** - Revenue analysis by period
  - **Purchase Report** - Cost analysis
  - **Inventory Report** - Current stock levels
  - **Low Stock Report** - Products below reorder level
- Date range filtering
- Download reports as PDF
- Interactive charts and visualizations

## Component Details

### Sidebar
- Collapsible navigation
- 8 main menu items
- User info display
- Logout button
- Responsive design

### Navbar
- Application title
- User dropdown menu
- Current user display
- Logout quick action

### Table Component
- Client-side search
- Sortable columns
- Pagination (customizable page size)
- Edit/Delete action buttons
- Empty state handling

### Modal Component
- Reusable dialog component
- Escape key to close
- Click outside to close
- Customizable title and content
- Three size options (sm, md, lg)

### Forms
- Product form with category/supplier selectors
- Category form (simple name + description)
- Supplier form with full contact details
- Client-side validation
- Error message display

### Charts
- Line charts for trends
- Bar charts for comparisons
- Pie/Doughnut charts for distributions
- Responsive and interactive
- Dark theme styled

## API Integration

All API calls are handled through the centralized `services/api.js`:

### Auth Endpoints
- `POST /auth/login` - User login
- `POST /auth/register` - User registration

### CRUD Endpoints
- `GET /products` - List all products
- `POST /products` - Create product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product
- Similar patterns for categories, suppliers, inventory

### Transaction Endpoints
- `POST /sales` - Create sale
- `POST /purchases` - Create purchase
- `GET /sales` - List sales
- `GET /purchases` - List purchases

### Report Endpoints
- `GET /reports/sales` - Sales data
- `GET /reports/purchases` - Purchase data
- `GET /reports/inventory` - Inventory snapshot
- `GET /reports/low-stock` - Low stock items
- `GET /reports/{type}/pdf` - Download PDF

### Dashboard Endpoints
- `GET /dashboard/summary` - KPI and chart data

## Styling

The application uses a modern dark theme with:
- **Primary Color**: #3b82f6 (Blue)
- **Secondary Color**: #10b981 (Green)
- **Danger Color**: #ef4444 (Red)
- **Warning Color**: #f59e0b (Amber)

CSS Custom Properties (CSS Variables) are used throughout for:
- Colors
- Spacing
- Typography
- Transitions

## Responsive Design

- Mobile-first approach
- Breakpoints:
  - `> 1024px` - Full layout
  - `768px - 1024px` - Tablet optimization
  - `< 768px` - Mobile layout
- Sidebar collapses on mobile
- Tables become scrollable

## Error Handling

- Try-catch blocks for all API calls
- User-friendly error messages
- Loading states during async operations
- Form validation before submission
- Automatic error alert dismissal

## Performance Optimizations

- Code splitting via React Router
- Lazy loading where applicable
- Memoization of expensive computations
- Optimized re-renders with React hooks
- CSS minification and bundling

## Development Tips

### Adding a New Page
1. Create component in `src/pages/NewPage.jsx`
2. Import API services from `services/api.js`
3. Add route to `App.jsx`
4. Add menu item to Sidebar

### Adding a New Component
1. Create in `src/components/`
2. Keep components focused and reusable
3. Use CSS modules or styled-components if needed
4. Document props with JSDoc comments

### API Debugging
- Check browser Network tab for requests
- Verify Authorization header is sent
- Check API response status codes
- Console logs in interceptors for debugging

## Troubleshooting

### API requests failing
- Ensure backend is running on port 8080
- Check CORS headers from backend
- Verify JWT token is valid
- Check browser console for errors

### Sidebar not appearing
- Ensure Sidebar component is imported
- Check CSS is loaded correctly
- Verify app-layout div in App.jsx

### Charts not rendering
- Ensure Chart.js is properly registered
- Check data format matches chart expectations
- Verify chart containers have height

## Browser Support

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Mobile browsers (iOS Safari, Chrome Android)

## License

Part of Smart Inventory Management System project.
