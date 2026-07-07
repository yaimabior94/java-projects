import React, { useEffect, useState } from 'react';
import {
  Chart as ChartJS,
  CategoryScale, LinearScale, BarElement,
  LineElement, PointElement, ArcElement,
  Title, Tooltip, Legend, Filler,
} from 'chart.js';
import { Bar, Doughnut } from 'react-chartjs-2';
import { dashboardApi, reportsApi } from '../services/api';

ChartJS.register(
  CategoryScale, LinearScale, BarElement,
  LineElement, PointElement, ArcElement,
  Title, Tooltip, Legend, Filler
);

const CHART_OPTS = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { labels: { color: '#9ca3af', font: { size: 12 } } } },
  scales: {
    x: { ticks: { color: '#6b7280' }, grid: { color: 'rgba(255,255,255,0.04)' } },
    y: { ticks: { color: '#6b7280' }, grid: { color: 'rgba(255,255,255,0.04)' } },
  },
};

const DOUGHNUT_OPTS = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { labels: { color: '#9ca3af', font: { size: 12 } } } },
};

const MONTHS = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
const PALETTE = ['#3b82f6','#10b981','#f59e0b','#8b5cf6','#ef4444','#06b6d4','#84cc16','#f97316'];

function KpiCard({ icon, label, value, sub, color }) {
  return (
    <div className="glass-card glass-card-interactive" style={{ position: 'relative', overflow: 'hidden' }}>
      <div style={{
        position: 'absolute', top: '-20px', right: '-20px',
        width: '90px', height: '90px', borderRadius: '50%',
        background: `${color}18`, filter: 'blur(20px)',
      }} />
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <p style={{ fontSize: '0.82rem', color: 'var(--text-muted)', fontWeight: 500, marginBottom: '0.5rem' }}>{label}</p>
          <p style={{ fontSize: '2rem', fontFamily: 'var(--font-title)', fontWeight: 700, color: '#fff', lineHeight: 1 }}>{value}</p>
          {sub && <p style={{ fontSize: '0.75rem', color, marginTop: '0.4rem' }}>{sub}</p>}
        </div>
        <div style={{
          width: '48px', height: '48px', borderRadius: '12px',
          background: `${color}18`, border: `1px solid ${color}35`,
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          fontSize: '1.4rem',
        }}>{icon}</div>
      </div>
    </div>
  );
}

export default function Dashboard() {
  const [summary, setSummary] = useState(null);
  const [inventoryData, setInventoryData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // BUG-10 FIX: removed reportsApi.getSales() — it returns per-sale rows, not monthly totals.
        // Monthly sales data is already included in the dashboard summary (summary.monthlySales).
        const [summaryRes, inventoryRes] = await Promise.all([
          dashboardApi.getSummary(),
          reportsApi.getInventory()
        ]);
        setSummary(summaryRes.data);
        setInventoryData(inventoryRes.data);
      } catch (err) {
        console.error('Error fetching dashboard data:', err);
        setError(err.response?.data?.message || err.message || 'Failed to load dashboard data.');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#ffffff' }}>Loading...</div>;
  }

  // BUG-10 FIX: Use summary.monthlySales (MonthlyTotalDto[]) which has the correct
  // { year, month, count, total } shape instead of the full SalesReportDto[] list.
  const monthlySales = summary?.monthlySales || [];
  const salesChartData = {
    labels: monthlySales.length > 0
      ? monthlySales.map(d => `${MONTHS[(d.month || 1) - 1]} ${d.year}`)
      : MONTHS.slice(0, 6),
    datasets: [{
      label: 'Sales ($)',
      data: monthlySales.length > 0
        ? monthlySales.map(d => parseFloat(d.total) || 0)
        : [0, 0, 0, 0, 0, 0],
      backgroundColor: '#3b82f6',
      borderRadius: 6
    }]
  };

  // BUG-11 FIX: Aggregate inventory by 'categoryName' (not 'category') and compute
  // per-category totals for the doughnut chart.
  const categoryTotals = {};
  (inventoryData || []).forEach(item => {
    const cat = item.categoryName || 'Unknown';
    categoryTotals[cat] = (categoryTotals[cat] || 0) + parseFloat(item.stockValue || 0);
  });
  const categoryLabels = Object.keys(categoryTotals);
  const inventoryChartData = {
    labels: categoryLabels.length > 0 ? categoryLabels : ['No Data'],
    datasets: [{
      label: 'Stock Value ($)',
      data: categoryLabels.length > 0 ? Object.values(categoryTotals) : [1],
      backgroundColor: PALETTE.slice(0, Math.max(categoryLabels.length, 1)),
      borderWidth: 0
    }]
  };

  // BUG-12 FIX: totalRevenue comes as a BigDecimal string from Java — parse it as float
  // before calling toLocaleString() so comma/decimal formatting works correctly.
  const revenueDisplay = `$${parseFloat(summary?.totalRevenue || 0).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

  return (
    <div style={{ padding: '24px' }}>
      <h1 style={{ color: '#fff', marginBottom: '24px' }}>Dashboard</h1>

      {error && (
        <div style={{
          padding: '12px 16px', marginBottom: '16px',
          backgroundColor: 'rgba(239, 68, 68, 0.15)',
          border: '1px solid rgba(239, 68, 68, 0.4)',
          borderRadius: '8px', color: '#fca5a5', fontSize: '0.9rem'
        }}>
          ⚠️ {error}
        </div>
      )}

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '16px', marginBottom: '32px' }}>
        <KpiCard icon="📦" label="Total Products" value={summary?.totalProducts || 0} color="#3b82f6" />
        <KpiCard icon="🏷️" label="Categories" value={summary?.totalCategories || 0} color="#10b981" />
        <KpiCard icon="🚚" label="Suppliers" value={summary?.totalSuppliers || 0} color="#f59e0b" />
        <KpiCard icon="💰" label="Total Revenue" value={revenueDisplay} sub="All time" color="#8b5cf6" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '24px' }}>
        <div className="glass-card" style={{ padding: '20px' }}>
          <h3 style={{ color: '#fff', marginBottom: '16px' }}>Monthly Sales</h3>
          <div style={{ height: '300px' }}>
            <Bar data={salesChartData} options={CHART_OPTS} />
          </div>
        </div>

        <div className="glass-card" style={{ padding: '20px' }}>
          <h3 style={{ color: '#fff', marginBottom: '16px' }}>Inventory by Category</h3>
          <div style={{ height: '300px' }}>
            <Doughnut data={inventoryChartData} options={DOUGHNUT_OPTS} />
          </div>
        </div>
      </div>
    </div>
  );
}
