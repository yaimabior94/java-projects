import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
  Filler
} from 'chart.js';
import { Line, Bar, Pie, Doughnut } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

const chartOptions = {
  responsive: true,
  maintainAspectRatio: true,
  plugins: {
    legend: {
      labels: {
        color: '#e2e8f0',
        font: { size: 12 }
      }
    }
  },
  scales: {
    y: {
      ticks: { color: '#cbd5e1' },
      grid: { color: 'rgba(71, 85, 105, 0.2)' }
    },
    x: {
      ticks: { color: '#cbd5e1' },
      grid: { color: 'rgba(71, 85, 105, 0.2)' }
    }
  }
};

export const SalesChart = ({ data }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Sales',
        data: data?.values || [],
        borderColor: '#3b82f6',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        borderWidth: 2,
        fill: true,
        tension: 0.4
      }
    ]
  };

  return <Line data={chartData} options={chartOptions} />;
};

export const RevenueChart = ({ data }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Revenue',
        data: data?.values || [],
        backgroundColor: [
          '#3b82f6',
          '#10b981',
          '#f59e0b',
          '#ef4444',
          '#8b5cf6',
          '#06b6d4'
        ],
        borderRadius: 6
      }
    ]
  };

  return <Bar data={chartData} options={chartOptions} />;
};

export const InventoryChart = ({ data }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Inventory',
        data: data?.values || [],
        backgroundColor: '#10b981',
        borderColor: '#059669',
        borderWidth: 2,
        fill: true,
        tension: 0.4
      }
    ]
  };

  return <Line data={chartData} options={chartOptions} />;
};

export const CategoryDistribution = ({ data }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Distribution',
        data: data?.values || [],
        backgroundColor: [
          '#3b82f6',
          '#10b981',
          '#f59e0b',
          '#ef4444',
          '#8b5cf6',
          '#06b6d4'
        ],
        borderColor: '#1e293b',
        borderWidth: 2
      }
    ]
  };

  return <Doughnut data={chartData} options={chartOptions} />;
};

// NEW: Sales Bar Chart
export const SalesBarChart = ({ data }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Sales Amount',
        data: data?.values || [],
        backgroundColor: [
          '#3b82f6',
          '#06b6d4',
          '#10b981',
          '#f59e0b',
          '#ef4444',
          '#8b5cf6'
        ],
        borderRadius: 6,
        borderWidth: 0
      }
    ]
  };

  const options = {
    ...chartOptions,
    scales: {
      ...chartOptions.scales,
      y: {
        ...chartOptions.scales.y,
        beginAtZero: true
      }
    }
  };

  return <Bar data={chartData} options={options} />;
};

// NEW: Purchases Line Chart
export const PurchasesLineChart = ({ data }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Purchase Orders',
        data: data?.values || [],
        borderColor: '#f59e0b',
        backgroundColor: 'rgba(245, 158, 11, 0.1)',
        borderWidth: 3,
        fill: true,
        tension: 0.4,
        pointBackgroundColor: '#f59e0b',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 7
      }
    ]
  };

  return <Line data={chartData} options={chartOptions} />;
};

// NEW: Categories Pie Chart
export const CategoriesPieChart = ({ data }) => {
  const chartData = {
    labels: data?.labels || [],
    datasets: [
      {
        label: 'Products by Category',
        data: data?.values || [],
        backgroundColor: [
          '#3b82f6',
          '#10b981',
          '#f59e0b',
          '#ef4444',
          '#8b5cf6',
          '#06b6d4',
          '#ec4899',
          '#14b8a6'
        ],
        borderColor: '#1e293b',
        borderWidth: 2
      }
    ]
  };

  const options = {
    ...chartOptions,
    plugins: {
      ...chartOptions.plugins,
      legend: {
        position: 'bottom',
        labels: {
          color: '#e2e8f0',
          font: { size: 12 },
          padding: 15
        }
      }
    }
  };

  return <Pie data={chartData} options={options} />;
};

// NEW: Low Stock Widget
export const LowStockWidget = ({ products = [] }) => {
  const lowStockItems = products
    .filter(p => p.currentStock <= p.reorderLevel)
    .sort((a, b) => a.currentStock - b.currentStock)
    .slice(0, 5);

  return (
    <div className="low-stock-widget">
      <div className="widget-header">
        <h3>⚠️ Low Stock Alert</h3>
        <span className="alert-count">{lowStockItems.length}</span>
      </div>
      
      <div className="widget-content">
        {lowStockItems.length === 0 ? (
          <p className="no-data">All items are in stock</p>
        ) : (
          <ul className="stock-list">
            {lowStockItems.map((item) => (
              <li key={item.id} className="stock-item">
                <div className="item-info">
                  <p className="item-name">{item.name}</p>
                  <p className="item-sku">SKU: {item.sku}</p>
                </div>
                <div className="item-stock">
                  <span className="current">
                    Current: <strong>{item.currentStock}</strong>
                  </span>
                  <span className="reorder">
                    Reorder: <strong>{item.reorderLevel}</strong>
                  </span>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>

      <div className="widget-footer">
        <button className="btn-view-all">View All Low Stock Items</button>
      </div>
    </div>
  );
};

// NEW: Monthly Revenue Card
export const MonthlyRevenueCard = ({ 
  currentMonth = 0, 
  previousMonth = 0, 
  monthName = 'This Month' 
}) => {
  const percentageChange = previousMonth > 0 
    ? (((currentMonth - previousMonth) / previousMonth) * 100).toFixed(1)
    : 0;
  
  const isPositive = percentageChange >= 0;

  return (
    <div className="revenue-card">
      <div className="card-header">
        <div className="title-section">
          <h3>Monthly Revenue</h3>
          <span className="month-label">{monthName}</span>
        </div>
        <div className={`trend-indicator ${isPositive ? 'positive' : 'negative'}`}>
          {isPositive ? '📈' : '📉'}
        </div>
      </div>

      <div className="card-body">
        <div className="revenue-display">
          <span className="label">Current Revenue</span>
          <h2 className="amount">
            ${currentMonth.toLocaleString('en-US', { 
              minimumFractionDigits: 2,
              maximumFractionDigits: 2 
            })}
          </h2>
        </div>

        <div className="comparison">
          <div className="comparison-item">
            <span className="comp-label">Previous Month</span>
            <span className="comp-value">
              ${previousMonth.toLocaleString('en-US', { 
                minimumFractionDigits: 2,
                maximumFractionDigits: 2 
              })}
            </span>
          </div>

          <div className={`comparison-item trend ${isPositive ? 'positive' : 'negative'}`}>
            <span className="comp-label">Change</span>
            <span className="comp-value">
              {isPositive ? '+' : ''}{percentageChange}%
            </span>
          </div>
        </div>

        {currentMonth > 0 && (
          <div className="progress-bar">
            <div 
              className="progress-fill"
              style={{ width: `${Math.min((currentMonth / (previousMonth || currentMonth * 1.5)) * 100, 100)}%` }}
            ></div>
          </div>
        )}
      </div>

      <div className="card-footer">
        <button className="btn-detail">View Detailed Report</button>
      </div>
    </div>
  );
};
