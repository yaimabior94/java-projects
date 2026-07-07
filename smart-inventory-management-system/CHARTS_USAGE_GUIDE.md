# Chart.js Components - Usage Guide

## Overview

Five new Chart.js components have been created for the Smart Inventory Management System:

1. **SalesBarChart** - Bar chart for sales data
2. **PurchasesLineChart** - Line chart for purchase trends
3. **CategoriesPieChart** - Pie chart for category distribution
4. **LowStockWidget** - Low stock alert widget
5. **MonthlyRevenueCard** - Revenue summary card

All components are fully responsive and use the dark theme styling.

---

## Components

### 1. SalesBarChart

**Purpose:** Display sales data in a colorful bar chart format.

**Props:**
```javascript
{
  data: {
    labels: ['Jan', 'Feb', 'Mar', ...],  // X-axis labels
    values: [12000, 19000, 15000, ...]   // Bar values
  }
}
```

**Usage Example:**
```jsx
import { SalesBarChart } from '../components/Charts';

function SalesPage() {
  const salesData = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    values: [12000, 19000, 15000, 25000, 22000, 30000]
  };

  return (
    <div className="chart-container">
      <div className="chart-header">
        <h3>📊 Sales by Month</h3>
      </div>
      <div className="sales-bar-chart">
        <SalesBarChart data={salesData} />
      </div>
    </div>
  );
}
```

**Features:**
- Colorful bars (6 different colors)
- Rounded corners
- Responsive sizing
- Hover effects
- Legend support

---

### 2. PurchasesLineChart

**Purpose:** Display purchase trends over time with a line chart.

**Props:**
```javascript
{
  data: {
    labels: ['Week 1', 'Week 2', 'Week 3', ...],  // X-axis labels
    values: [45, 52, 48, 61, ...]                  // Line values
  }
}
```

**Usage Example:**
```jsx
import { PurchasesLineChart } from '../components/Charts';

function PurchasesReport() {
  const purchasesData = {
    labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
    values: [45, 52, 48, 61]
  };

  return (
    <div className="chart-container">
      <div className="chart-header">
        <h3>📈 Purchase Trends</h3>
      </div>
      <div className="purchases-line-chart">
        <PurchasesLineChart data={purchasesData} />
      </div>
    </div>
  );
}
```

**Features:**
- Amber/orange line color
- Filled area under line
- Point markers with hover effects
- Smooth curve tension
- Grid lines
- Legend support

---

### 3. CategoriesPieChart

**Purpose:** Show product distribution across categories in a pie chart.

**Props:**
```javascript
{
  data: {
    labels: ['Electronics', 'Furniture', 'Clothing', ...],  // Category names
    values: [24, 18, 32, ...]                                // Product counts
  }
}
```

**Usage Example:**
```jsx
import { CategoriesPieChart } from '../components/Charts';

function CategoriesAnalysis() {
  const categoriesData = {
    labels: ['Electronics', 'Furniture', 'Clothing', 'Books', 'Toys'],
    values: [24, 18, 32, 15, 11]
  };

  return (
    <div className="chart-container">
      <div className="chart-header">
        <h3>🎯 Product Distribution by Category</h3>
      </div>
      <div className="categories-pie-chart">
        <CategoriesPieChart data={categoriesData} />
      </div>
    </div>
  );
}
```

**Features:**
- Multiple colors (8 different colors)
- Legend at bottom
- Responsive sizing
- Hover tooltips
- Percentage display

---

### 4. LowStockWidget

**Purpose:** Display alert for products with low stock levels.

**Props:**
```javascript
{
  products: [
    {
      id: 1,
      name: 'Product Name',
      sku: 'SKU-001',
      currentStock: 2,
      reorderLevel: 10
    },
    // ... more products
  ]
}
```

**Usage Example:**
```jsx
import { LowStockWidget } from '../components/Charts';

function InventoryDashboard() {
  const lowStockProducts = [
    {
      id: 1,
      name: 'Laptop Stand',
      sku: 'LS-001',
      currentStock: 2,
      reorderLevel: 10
    },
    {
      id: 2,
      name: 'USB-C Cable',
      sku: 'UC-002',
      currentStock: 5,
      reorderLevel: 50
    }
  ];

  return (
    <div style={{ width: '100%', maxWidth: '500px' }}>
      <LowStockWidget products={lowStockProducts} />
    </div>
  );
}
```

**Features:**
- Alert count badge
- Scrollable item list
- Current vs reorder level display
- Color-coded stock levels
- "View All" button
- Empty state handling

---

### 5. MonthlyRevenueCard

**Purpose:** Display monthly revenue with comparison to previous month and trend indicator.

**Props:**
```javascript
{
  currentMonth: 98500,      // Current month revenue (number)
  previousMonth: 85000,     // Previous month revenue (number)
  monthName: 'July 2024'    // Display month name (string)
}
```

**Usage Example:**
```jsx
import { MonthlyRevenueCard } from '../components/Charts';

function ReportsDashboard() {
  return (
    <div style={{ width: '100%', maxWidth: '400px' }}>
      <MonthlyRevenueCard
        currentMonth={98500}
        previousMonth={85000}
        monthName="July 2024"
      />
    </div>
  );
}
```

**Features:**
- Large, formatted revenue display
- Percentage change calculation
- Trend indicator (📈 or 📉)
- Previous month comparison
- Progress bar visualization
- Positive/negative trend styling
- "View Detailed Report" button

---

## Integration Examples

### Example 1: Add to Dashboard Page

```jsx
// src/pages/Dashboard.jsx
import React, { useState, useEffect } from 'react';
import {
  SalesBarChart,
  PurchasesLineChart,
  CategoriesPieChart,
  LowStockWidget,
  MonthlyRevenueCard
} from '../components/Charts';
import { dashboardApi } from '../services/api';
import '../styles/Charts.css';

export default function Dashboard() {
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await dashboardApi.getSummary();
        setDashboardData(data);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div style={{ padding: '20px' }}>
      <h1>Dashboard</h1>

      {/* Revenue Card */}
      <div style={{ marginBottom: '30px', maxWidth: '400px' }}>
        <MonthlyRevenueCard
          currentMonth={dashboardData?.currentRevenue}
          previousMonth={dashboardData?.previousRevenue}
          monthName="This Month"
        />
      </div>

      {/* Charts Row */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
        <div className="chart-container">
          <SalesBarChart data={dashboardData?.salesData} />
        </div>
        <div className="chart-container">
          <PurchasesLineChart data={dashboardData?.purchasesData} />
        </div>
      </div>

      {/* Low Stock & Categories */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginTop: '20px' }}>
        <div className="chart-container">
          <LowStockWidget products={dashboardData?.lowStockProducts} />
        </div>
        <div className="chart-container">
          <CategoriesPieChart data={dashboardData?.categoriesData} />
        </div>
      </div>
    </div>
  );
}
```

### Example 2: Add to Reports Page

```jsx
// src/pages/Reports.jsx
import { SalesBarChart, PurchasesLineChart } from '../components/Charts';
import { reportsApi } from '../services/api';

export default function Reports() {
  const [reportData, setReportData] = useState(null);

  useEffect(() => {
    async function loadReports() {
      const data = await reportsApi.getSales();
      setReportData(data);
    }
    loadReports();
  }, []);

  return (
    <div>
      <h2>Sales Reports</h2>
      <div className="chart-container">
        <SalesBarChart data={reportData?.byMonth} />
      </div>

      <h2>Purchase Reports</h2>
      <div className="chart-container">
        <PurchasesLineChart data={reportData?.byWeek} />
      </div>
    </div>
  );
}
```

### Example 3: Add to Inventory Page

```jsx
// src/pages/Inventory.jsx
import { LowStockWidget } from '../components/Charts';
import { inventoryApi } from '../services/api';

export default function Inventory() {
  const [inventory, setInventory] = useState([]);

  useEffect(() => {
    async function loadInventory() {
      const data = await inventoryApi.getAll();
      setInventory(data);
    }
    loadInventory();
  }, []);

  return (
    <div>
      <h2>Inventory Management</h2>
      
      {/* Low Stock Widget */}
      <div style={{ marginBottom: '30px', maxWidth: '500px' }}>
        <LowStockWidget products={inventory} />
      </div>

      {/* Rest of inventory page */}
    </div>
  );
}
```

---

## CSS Classes

All components come with pre-defined CSS classes:

```css
.chart-container          /* Main chart wrapper */
.chart-header            /* Chart title section */
.chart-period           /* Period label */
.sales-bar-chart        /* Sales chart container */
.purchases-line-chart   /* Purchases chart container */
.categories-pie-chart   /* Categories chart container */
.low-stock-widget       /* Low stock widget */
.revenue-card           /* Revenue card */
```

---

## Styling

### Default Colors

The components use the dark theme color scheme:

- **Primary:** #3b82f6 (Blue)
- **Secondary:** #10b981 (Green)
- **Danger:** #ef4444 (Red)
- **Warning:** #f59e0b (Amber)
- **Background Primary:** #0f172a
- **Background Secondary:** #1e293b
- **Text Primary:** #ffffff

### Customization

To customize colors, edit the CSS variables in `src/index.css`:

```css
:root {
  --primary: #3b82f6;
  --secondary: #10b981;
  --danger: #ef4444;
  --warning: #f59e0b;
  --bg-primary: #0f172a;
  --bg-secondary: #1e293b;
  --text-primary: #ffffff;
}
```

---

## Data Format

### Bar Chart Data
```javascript
{
  labels: ['Jan', 'Feb', 'Mar'],
  values: [100, 200, 150]
}
```

### Line Chart Data
```javascript
{
  labels: ['Week 1', 'Week 2', 'Week 3'],
  values: [45, 52, 48]
}
```

### Pie Chart Data
```javascript
{
  labels: ['Category 1', 'Category 2', 'Category 3'],
  values: [30, 25, 45]
}
```

### Low Stock Widget Data
```javascript
[
  {
    id: 1,
    name: 'Product Name',
    sku: 'SKU-001',
    currentStock: 5,
    reorderLevel: 20
  }
]
```

### Revenue Card Data
```javascript
{
  currentMonth: 98500,
  previousMonth: 85000,
  monthName: 'July 2024'
}
```

---

## Responsive Design

All components are fully responsive:

- **Desktop:** Full-width with multiple columns
- **Tablet:** 2-column grid
- **Mobile:** Single column, stacked layout

```jsx
// Example responsive layout
<div style={{
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))',
  gap: '20px'
}}>
  <div className="chart-container">
    <SalesBarChart data={salesData} />
  </div>
  <div className="chart-container">
    <PurchasesLineChart data={purchasesData} />
  </div>
</div>
```

---

## Performance Tips

1. **Memoize Data:** Use `useMemo` to prevent unnecessary re-renders

```jsx
const salesData = useMemo(() => ({
  labels: [...],
  values: [...]
}), [dependencies]);
```

2. **Lazy Load Charts:** Load chart data only when needed

```jsx
useEffect(() => {
  const timer = setTimeout(() => {
    fetchChartData();
  }, 300);
  return () => clearTimeout(timer);
}, []);
```

3. **Pagination:** For large datasets, implement pagination

```jsx
const itemsPerPage = 10;
const paginatedProducts = lowStockProducts.slice(0, itemsPerPage);
```

---

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)
- Mobile browsers

---

## Dependencies

- `chart.js` ^4.4.3
- `react-chartjs-2` ^5.2.0
- React 18+

---

## File Locations

| File | Location |
|------|----------|
| Chart Components | `src/components/Charts.jsx` |
| Styling | `src/styles/Charts.css` |
| Demo Component | `src/components/DashboardChartsDemo.jsx` |

---

## Troubleshooting

### Chart not displaying

**Solution:** Ensure Chart.js is registered in Charts.jsx:

```javascript
import ChartJS from 'chart.js/auto';
ChartJS.register(...);
```

### Data not updating

**Solution:** Use `useEffect` to fetch data and trigger re-renders:

```javascript
useEffect(() => {
  fetchChartData();
}, [chartType]); // Add dependencies
```

### Styling issues

**Solution:** Import CSS file in your component:

```javascript
import '../styles/Charts.css';
```

### Low stock widget showing "No data"

**Solution:** Ensure product data has required fields:

```javascript
{
  id, name, sku, currentStock, reorderLevel
}
```

---

## Next Steps

1. Import components into your pages
2. Fetch data from API endpoints
3. Pass data to chart components
4. Customize styling as needed
5. Test responsiveness on different devices

---

**Happy charting! 📊📈**
