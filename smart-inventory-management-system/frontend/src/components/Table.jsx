import React, { useState, useMemo } from 'react';
import '../styles/Table.css';

/**
 * Reusable Table component with search, sort, and pagination
 * 
 * Props:
 *   columns: [{ key, label, render? }]
 *   data: array of objects
 *   onEdit?: (row) => void
 *   onDelete?: (row) => void
 *   pageSize?: number (default 10)
 */
const Table = ({ columns = [], data = [], onEdit, onDelete, pageSize = 10 }) => {
  const [search, setSearch] = useState('');
  const [sortKey, setSortKey] = useState(null);
  const [sortDir, setSortDir] = useState('asc');
  const [page, setPage] = useState(1);

  // Filter data
  const filtered = useMemo(() => {
    if (!search.trim()) return data;
    const q = search.toLowerCase();
    return data.filter((row) =>
      columns.some((col) => 
        String(row[col.key] ?? '').toLowerCase().includes(q)
      )
    );
  }, [data, search, columns]);

  // Sort data
  const sorted = useMemo(() => {
    if (!sortKey) return filtered;
    return [...filtered].sort((a, b) => {
      const av = a[sortKey] ?? '';
      const bv = b[sortKey] ?? '';
      const cmp = String(av).localeCompare(String(bv), undefined, { numeric: true });
      return sortDir === 'asc' ? cmp : -cmp;
    });
  }, [filtered, sortKey, sortDir]);

  // Paginate
  const totalPages = Math.ceil(sorted.length / pageSize) || 1;
  const safePage = Math.min(page, totalPages);
  const paged = sorted.slice((safePage - 1) * pageSize, safePage * pageSize);

  const handleSort = (key) => {
    if (sortKey === key) {
      setSortDir(d => d === 'asc' ? 'desc' : 'asc');
    } else {
      setSortKey(key);
      setSortDir('asc');
    }
    setPage(1);
  };

  return (
    <div>
      <div style={{ marginBottom: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <input
          type="text"
          placeholder="Search..."
          value={search}
          onChange={(e) => { setSearch(e.target.value); setPage(1); }}
          style={{
            padding: '8px 12px',
            borderRadius: '6px',
            border: '1px solid var(--border)',
            backgroundColor: 'var(--bg-tertiary)',
            color: 'var(--text-primary)',
            maxWidth: '300px'
          }}
        />
        <span style={{ fontSize: '12px', color: 'var(--text-tertiary)' }}>
          {filtered.length} records
        </span>
      </div>

      <div className="table-wrapper">
        {paged.length === 0 ? (
          <div className="table-empty">No data available</div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                {columns.map((col) => (
                  <th
                    key={col.key}
                    onClick={() => handleSort(col.key)}
                    style={{ cursor: 'pointer', userSelect: 'none' }}
                  >
                    {col.label}
                    {sortKey === col.key && (
                      <span style={{ marginLeft: '6px' }}>
                        {sortDir === 'asc' ? '▲' : '▼'}
                      </span>
                    )}
                  </th>
                ))}
                {(onEdit || onDelete) && <th>Actions</th>}
              </tr>
            </thead>
            <tbody>
              {paged.map((row, idx) => (
                <tr key={idx}>
                  {columns.map((col) => (
                    <td key={col.key}>
                      {col.render ? col.render(row) : row[col.key]}
                    </td>
                  ))}
                  {(onEdit || onDelete) && (
                    <td>
                      <div className="table-actions">
                        {onEdit && (
                          <button className="btn-edit" onClick={() => onEdit(row)}>
                            ✎ Edit
                          </button>
                        )}
                        {onDelete && (
                          <button className="btn-delete" onClick={() => onDelete(row)}>
                            🗑 Delete
                          </button>
                        )}
                      </div>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {totalPages > 1 && (
        <div style={{ marginTop: '16px', display: 'flex', justifyContent: 'center', gap: '8px' }}>
          <button
            onClick={() => setPage(p => Math.max(1, p - 1))}
            disabled={page === 1}
            style={{
              padding: '6px 12px',
              cursor: page === 1 ? 'not-allowed' : 'pointer',
              opacity: page === 1 ? 0.5 : 1
            }}
          >
            ← Prev
          </button>
          {Array.from({ length: totalPages }, (_, i) => i + 1).map((p) => (
            <button
              key={p}
              onClick={() => setPage(p)}
              style={{
                padding: '6px 12px',
                backgroundColor: page === p ? 'var(--primary)' : 'var(--bg-tertiary)',
                color: 'var(--text-primary)',
                border: '1px solid var(--border)',
                borderRadius: '4px',
                cursor: 'pointer'
              }}
            >
              {p}
            </button>
          ))}
          <button
            onClick={() => setPage(p => Math.min(totalPages, p + 1))}
            disabled={page === totalPages}
            style={{
              padding: '6px 12px',
              cursor: page === totalPages ? 'not-allowed' : 'pointer',
              opacity: page === totalPages ? 0.5 : 1
            }}
          >
            Next →
          </button>
        </div>
      )}
    </div>
  );
};

export default Table;
