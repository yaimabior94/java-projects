// ======================================
// QUICK INTEGRATION SNIPPETS
// ======================================

// ====== SNIPPET 1: Import Charts ======
// Add this to the top of your component file

import {
  SalesBarChart,
  PurchasesLineChart,
  CategoriesPieChart,
  LowStockWidget,
  MonthlyRevenueCard
} from '../components/Charts';
import '../styles/Charts.css';


// ====== SNIPPET 2: Sample State Setup ======
// Add these states to your component

const [salesData, setSalesData] = useState({
  labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
  values: [12000, 19000, 15000, 25000, 22000, 30000]
});

const [purchasesData, setPurchasesData] = useState({
  labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
  values: [45, 52, 48, 61]
});

const [categoriesData, setCategoriesData] = useState({
  labels: ['Electronics', 'Furniture', 'Clothing', 'Books', 'Toys'],
  values: [24, 18, 32, 15, 11]
});

const [lowStockProducts, setLowStockProducts] = useState([
  {
    id: 1,
    name: 'Laptop Stand',
    sku: 'LS-001',
    currentStock: 2,
    reorderLevel: 10
  }
]);


// ====== SNIPPET 3: Sales Bar Chart JSX ======
// Copy this into your render section

<div className="chart-container">
  <div className="chart-header">
    <h3>📊 Sales by Month</h3>
    <span className="chart-period">2024</span>
  </div>
  <div className="sales-bar-chart">
    <SalesBarChart data={salesData} />
  </div>
</div>


// ====== SNIPPET 4: Purchases Line Chart JSX ======
// Copy this into your render section

<div className="chart-container">
  <div className="chart-header">
    <h3>📈 Purchase Trends</h3>
    <span className="chart-period">Q2 2024</span>
  </div>
  <div className="purchases-line-chart">
    <PurchasesLineChart data={purchasesData} />
  </div>
</div>


// ====== SNIPPET 5: Categories Pie Chart JSX ======
// Copy this into your render section

<div className="chart-container">
  <div className="chart-header">
    <h3>🎯 Product Distribution</h3>
    <span className="chart-period">All Time</span>
  </div>
  <div className="categories-pie-chart">
    <CategoriesPieChart data={categoriesData} />
  </div>
</div>


// ====== SNIPPET 6: Low Stock Widget JSX ======
// Copy this into your render section

<div className="chart-container">
  <LowStockWidget products={lowStockProducts} />
</div>


// ====== SNIPPET 7: Monthly Revenue Card JSX ======
// Copy this into your render section

<div className="chart-container">
  <MonthlyRevenueCard
    currentMonth={98500}
    previousMonth={85000}
    monthName="July 2024"
  />
</div>


// ====== SNIPPET 8: Responsive Grid Layout ======
// Wrap multiple charts in a responsive grid

<div style={{
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))',
  gap: '20px',
  marginTop: '30px'
}}>
  {/* Charts go here */}
</div>


// ====== SNIPPET 9: Fetch Data from API ======
// Add this useEffect to your component

useEffect(() => {
  const fetchChartData = async () => {
    try {
      // Fetch sales data
      const sales = await dashboardApi.getSummary();
      setSalesData(sales.salesByMonth);

      // Fetch purchases data
      const purchases = await reportsApi.getPurchases();
      setPurchasesData(purchases.byWeek);

      // Fetch categories data
      const categories = await categoriesApi.getAll();
      const categoryLabels = categories.map(c => c.name);
      const categoryValues = categories.map(c => c.productCount);
      setCategoriesData({
        labels: categoryLabels,
        values: categoryValues
      });

      // Fetch low stock products
      const inventory = await inventoryApi.getAll();
      const lowStock = inventory.filter(
        p => p.currentStock <= p.reorderLevel
      );
      setLowStockProducts(lowStock);
    } catch (error) {
      console.error('Error fetching chart data:', error);
    }
  };

  fetchChartData();
}, []);


// ====== SNIPPET 10: Complete Dashboard Example ======
// Full component example

import React, { useState, useEffect } from 'react';
import {
  SalesBarChart,
  PurchasesLineChart,
  CategoriesPieChart,
  LowStockWidget,
  MonthlyRevenueCard
} from '../components/Charts';
import {
  dashboardApi,
  reportsApi,
  categoriesApi,
  inventoryApi
} from '../services/api';
import '../styles/Charts.css';

export default function Dashboard() {
  const [salesData, setSalesData] = useState(null);
  const [purchasesData, setPurchasesData] = useState(null);
  const [categoriesData, setCategoriesData] = useState(null);
  const [lowStockProducts, setLowStockProducts] = useState([]);
  const [revenueStats, setRevenueStats] = useState({
    current: 0,
    previous: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch all required data
        const [summary, purchases, categories, inventory] = await Promise.all([
          dashboardApi.getSummary(),
          reportsApi.getPurchases(),
          categoriesApi.getAll(),
          inventoryApi.getAll()
        ]);

        // Set sales data
        setSalesData(summary.salesByMonth);

        // Set purchases data
        setPurchasesData(purchases.byWeek);

        // Set categories data
        setCategoriesData({
          labels: categories.map(c => c.name),
          values: categories.map(c => c.productCount)
        });

        // Set low stock products
        const lowStock = inventory.filter(
          p => p.currentStock <= p.reorderLevel
        );
        setLowStockProducts(lowStock);

        // Set revenue stats
        setRevenueStats({
          current: summary.currentRevenue,
          previous: summary.previousRevenue
        });
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return <div style={{ padding: '20px' }}>Loading dashboard...</div>;
  }

  return (
    <div style={{ padding: '20px' }}>
      <h1 style={{ color: '#fff', marginBottom: '30px' }}>Dashboard</h1>

      {/* Top Row: Revenue & Low Stock */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '20px',
        marginBottom: '30px'
      }}>
        <div className="chart-container">
          <MonthlyRevenueCard
            currentMonth={revenueStats.current}
            previousMonth={revenueStats.previous}
            monthName="This Month"
          />
        </div>
        <div className="chart-container">
          <LowStockWidget products={lowStockProducts} />
        </div>
      </div>

      {/* Middle Row: Sales & Purchases */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '20px',
        marginBottom: '30px'
      }}>
        <div className="chart-container">
          <div className="chart-header">
            <h3>📊 Sales by Month</h3>
            <span className="chart-period">2024</span>
          </div>
          <div className="sales-bar-chart">
            {salesData && <SalesBarChart data={salesData} />}
          </div>
        </div>
        <div className="chart-container">
          <div className="chart-header">
            <h3>📈 Purchase Trends</h3>
            <span className="chart-period">Q2 2024</span>
          </div>
          <div className="purchases-line-chart">
            {purchasesData && <PurchasesLineChart data={purchasesData} />}
          </div>
        </div>
      </div>

      {/* Bottom Row: Categories */}
      <div className="chart-container">
        <div className="chart-header">
          <h3>🎯 Product Distribution by Category</h3>
          <span className="chart-period">All Time</span>
        </div>
        <div className="categories-pie-chart">
          {categoriesData && <CategoriesPieChart data={categoriesData} />}
        </div>
      </div>
    </div>
  );
}


// ====== SNIPPET 11: Styling for Charts Container ======
// Add to your component's style or CSS file

const chartContainerStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))',
  gap: '20px',
  marginTop: '30px',
  marginBottom: '30px'
};

const chartStyle = {
  backgroundColor: 'var(--bg-secondary)',
  border: '1px solid rgba(71, 85, 105, 0.3)',
  borderRadius: '8px',
  padding: '20px'
};


// ====== SNIPPET 12: Error Boundary Wrapper ======
// Add error handling to charts

<div className="chart-container" style={{ position: 'relative' }}>
  {salesData ? (
    <SalesBarChart data={salesData} />
  ) : (
    <div style={{ 
      display: 'flex', 
      alignItems: 'center', 
      justifyContent: 'center',
      height: '300px',
      color: '#94a3b8'
    }}>
      No data available
    </div>
  )}
</div>


// ====== SNIPPET 13: Real-time Updates ======
// Set up data refresh interval

useEffect(() => {
  const interval = setInterval(() => {
    fetchChartData();
  }, 30000); // Refresh every 30 seconds

  return () => clearInterval(interval);
}, []);


// ====== SNIPPET 14: Export Chart Data ======
// Add button to download chart data

const exportChartData = (data, fileName) => {
  const json = JSON.stringify(data, null, 2);
  const blob = new Blob([json], { type: 'application/json' });
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = fileName;
  a.click();
};

// Usage:
// <button onClick={() => exportChartData(salesData, 'sales-data.json')}>
//   Download Sales Data
// </button>


// ====== SNIPPET 15: Responsive Mobile Layout ======
// Mobile-optimized grid

<div style={{
  display: 'grid',
  gridTemplateColumns: window.innerWidth > 768 ? '1fr 1fr' : '1fr',
  gap: '20px'
}}>
  {/* Charts */}
</div>
