# 📊 Chart.js - QUICK REFERENCE CARD

## 🚀 30-SECOND SETUP

```javascript
// 1. Import
import { SalesBarChart, PurchasesLineChart, CategoriesPieChart, LowStockWidget, MonthlyRevenueCard } from '../components/Charts';
import '../styles/Charts.css';

// 2. Add to JSX
<div className="chart-container">
  <SalesBarChart data={{ labels: ['Jan','Feb','Mar'], values: [100,200,150] }} />
</div>

// 3. Done! ✅
```

---

## 📊 COMPONENTS

### SalesBarChart
```jsx
<SalesBarChart data={{
  labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
  values: [12000, 19000, 15000, 25000, 22000, 30000]
}} />
```

### PurchasesLineChart
```jsx
<PurchasesLineChart data={{
  labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
  values: [45, 52, 48, 61]
}} />
```

### CategoriesPieChart
```jsx
<CategoriesPieChart data={{
  labels: ['Electronics', 'Furniture', 'Clothing', 'Books'],
  values: [24, 18, 32, 15]
}} />
```

### LowStockWidget
```jsx
<LowStockWidget products={[
  {
    id: 1,
    name: 'Laptop Stand',
    sku: 'LS-001',
    currentStock: 2,
    reorderLevel: 10
  }
]} />
```

### MonthlyRevenueCard
```jsx
<MonthlyRevenueCard
  currentMonth={98500}
  previousMonth={85000}
  monthName="July 2024"
/>
```

---

## 🎨 CSS CLASSES

```css
.chart-container              /* Main wrapper */
.chart-header                 /* Title section */
.chart-period                /* Period label */
.sales-bar-chart             /* Bar chart */
.purchases-line-chart        /* Line chart */
.categories-pie-chart        /* Pie chart */
.low-stock-widget            /* Low stock */
.revenue-card                /* Revenue card */
```

---

## 📁 FILES

| File | Purpose |
|------|---------|
| `src/components/Charts.jsx` | All 5 components |
| `src/styles/Charts.css` | All styling |
| `CHARTS_USAGE_GUIDE.md` | Full documentation |
| `CHARTS_QUICK_SNIPPETS.md` | Code snippets |
| `CHARTS_VISUAL_GUIDE.md` | Visual reference |

---

## 💡 COMMON USAGE

### On Dashboard
```jsx
<div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
  <MonthlyRevenueCard currentMonth={98500} previousMonth={85000} monthName="This Month" />
  <LowStockWidget products={lowStockProducts} />
</div>
<div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
  <div className="chart-container"><SalesBarChart data={salesData} /></div>
  <div className="chart-container"><PurchasesLineChart data={purchasesData} /></div>
</div>
```

### On Reports
```jsx
<div className="chart-container">
  <SalesBarChart data={monthlyData} />
</div>
<div className="chart-container">
  <CategoriesPieChart data={categoryData} />
</div>
```

### On Inventory
```jsx
<LowStockWidget products={inventory.filter(p => p.currentStock <= p.reorderLevel)} />
```

---

## 🔄 FETCH & DISPLAY

```jsx
useEffect(() => {
  const fetchData = async () => {
    const summary = await dashboardApi.getSummary();
    setSalesData(summary.salesByMonth);
  };
  fetchData();
}, []);
```

---

## 🎯 DATA FORMATS

```javascript
// Charts
{ labels: [...], values: [...] }

// Low Stock
[{ id, name, sku, currentStock, reorderLevel }]

// Revenue
{ currentMonth: 98500, previousMonth: 85000, monthName: 'July' }
```

---

## 🎨 COLORS

```
Blue:    #3b82f6
Green:   #10b981
Red:     #ef4444
Amber:   #f59e0b
Cyan:    #06b6d4
Purple:  #8b5cf6
```

---

## 📱 RESPONSIVE

```jsx
// Auto-responsive grid
<div style={{
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))',
  gap: '20px'
}}>
  {/* Charts automatically adjust */}
</div>
```

---

## ✅ CHECKLIST

- [ ] Import components
- [ ] Import CSS file
- [ ] Prepare data in state
- [ ] Fetch data in useEffect
- [ ] Add to JSX
- [ ] Wrap in `.chart-container`
- [ ] Test with sample data
- [ ] Customize colors (optional)
- [ ] Test on mobile
- [ ] Deploy ✅

---

## 🔧 TROUBLESHOOTING

| Problem | Solution |
|---------|----------|
| Chart not showing | Check data format, import CSS |
| Data not updating | Add useEffect, check API |
| Styling broken | Import `../styles/Charts.css` |
| Low stock empty | Filter with `currentStock <= reorderLevel` |

---

## 📞 DOCS

```
📖 Full Guide: CHARTS_USAGE_GUIDE.md
📋 Snippets: CHARTS_QUICK_SNIPPETS.md
📊 Summary: CHARTS_COMPLETE_SUMMARY.md
👁️ Visual: CHARTS_VISUAL_GUIDE.md
💻 Source: src/components/Charts.jsx
🎨 Styles: src/styles/Charts.css
```

---

## 🎓 LEARN MORE

```javascript
// Use useMemo for optimization
const data = useMemo(() => ({ ... }), []);

// Use useCallback for event handlers
const handleClick = useCallback(() => { ... }, []);

// Use error boundary for safety
<ErrorBoundary>
  <SalesBarChart data={data} />
</ErrorBoundary>
```

---

## 🚀 DEPLOY

```bash
npm run build      # Build for production
npm run dev        # Local development
npm run preview    # Preview build
```

---

**Status:** ✅ Production Ready  
**Version:** 1.0.0  
**Updated:** 2024  

**Ready to use! 🎉**

---

## KEY COMPONENTS AT A GLANCE

```
📊 SalesBarChart        → Sales visualization
📈 PurchasesLineChart   → Trend tracking
🎯 CategoriesPieChart   → Distribution
⚠️ LowStockWidget        → Inventory alerts
💰 MonthlyRevenueCard    → KPI summary
```

---

**Print this card, bookmark it, or save it for quick reference!** 📌
