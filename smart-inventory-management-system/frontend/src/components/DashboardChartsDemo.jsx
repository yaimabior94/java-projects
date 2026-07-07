import React, { useState, useEffect } from 'react';
import {
  SalesBarChart,
  PurchasesLineChart,
  CategoriesPieChart,
  LowStockWidget,
  MonthlyRevenueCard
} from './Charts';
import '../styles/Charts.css';

/**
 * Dashboard Charts Demo Component
 * 
 * This component demonstrates how to use all the new Chart.js components:
 * - SalesBarChart: Bar chart showing sales by period
 * - PurchasesLineChart: Line chart showing purchase trends
 * - CategoriesPieChart: Pie chart showing product distribution
 * - LowStockWidget: Low stock alert widget
 * - MonthlyRevenueCard: Revenue summary card
 */
export const DashboardChartsDemo = () => {
  const [salesData, setSalesData] = useState(null);
  const [purchasesData, setPurchasesData] = useState(null);
  const [categoriesData, setCategoriesData] = useState(null);
  const [lowStockProducts, setLowStockProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  // Sample data - Replace with API calls in production
  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      // Sales Bar Chart Data
      setSalesData({
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        values: [12000, 19000, 15000, 25000, 22000, 30000]
      });

      // Purchases Line Chart Data
      setPurchasesData({
        labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
        values: [45, 52, 48, 61]
      });

      // Categories Pie Chart Data
      setCategoriesData({
        labels: ['Electronics', 'Furniture', 'Clothing', 'Books', 'Toys'],
        values: [24, 18, 32, 15, 11]
      });

      // Low Stock Products
      setLowStockProducts([
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
        },
        {
          id: 3,
          name: 'Mouse Pad',
          sku: 'MP-003',
          currentStock: 3,
          reorderLevel: 20
        },
        {
          id: 4,
          name: 'Keyboard',
          sku: 'KB-004',
          currentStock: 1,
          reorderLevel: 15
        }
      ]);

      setLoading(false);
    }, 500);
  }, []);

  if (loading) {
    return (
      <div style={{ padding: '20px', textAlign: 'center' }}>
        <p>Loading charts...</p>
      </div>
    );
  }

  return (
    <div style={{ padding: '20px', backgroundColor: '#0f172a', minHeight: '100vh' }}>
      <h1 style={{ color: '#fff', marginBottom: '30px' }}>📊 Dashboard Charts & Widgets</h1>

      {/* Top Row: Revenue Card & Low Stock Widget */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: '20px',
          marginBottom: '30px'
        }}
      >
        {/* Monthly Revenue Card */}
        <div className="chart-container">
          <MonthlyRevenueCard
            currentMonth={98500}
            previousMonth={85000}
            monthName="July 2024"
          />
        </div>

        {/* Low Stock Widget */}
        <div className="chart-container">
          <LowStockWidget products={lowStockProducts} />
        </div>
      </div>

      {/* Middle Row: Sales & Purchases Charts */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: '20px',
          marginBottom: '30px'
        }}
      >
        {/* Sales Bar Chart */}
        <div className="chart-container">
          <div className="chart-header">
            <h3>📊 Sales by Month</h3>
            <span className="chart-period">2024</span>
          </div>
          <div className="sales-bar-chart">
            {salesData && <SalesBarChart data={salesData} />}
          </div>
        </div>

        {/* Purchases Line Chart */}
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

      {/* Bottom Row: Categories Pie Chart */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: '1fr',
          marginBottom: '30px'
        }}
      >
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
    </div>
  );
};

export default DashboardChartsDemo;
