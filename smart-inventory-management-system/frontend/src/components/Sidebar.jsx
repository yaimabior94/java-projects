import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/Sidebar.css';

const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(true);
  const { user, logout } = useAuth();
  const location = useLocation();

  const menuItems = [
    { path: '/', label: 'Dashboard', icon: '📊' },
    { path: '/products', label: 'Products', icon: '📦' },
    { path: '/categories', label: 'Categories', icon: '🏷️' },
    { path: '/suppliers', label: 'Suppliers', icon: '🚚' },
    { path: '/inventory', label: 'Inventory', icon: '📋' },
    { path: '/sales', label: 'Sales', icon: '💰', role: 'ADMIN' },
    { path: '/purchases', label: 'Purchases', icon: '🛒', role: 'ADMIN' },
    { path: '/reports', label: 'Reports', icon: '📈' },
  ].filter(item => !item.role || (user && user.role === item.role));

  return (
    <aside className={`sidebar ${isOpen ? 'open' : 'closed'}`}>
      <div className="sidebar-header">
        <button 
          className="toggle-btn"
          onClick={() => setIsOpen(!isOpen)}
          title={isOpen ? 'Close sidebar' : 'Open sidebar'}
        >
          ☰
        </button>
        {isOpen && <h2 className="sidebar-title">Smart Inventory</h2>}
      </div>

      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`nav-link ${location.pathname === item.path ? 'active' : ''}`}
            title={item.label}
          >
            <span className="nav-icon">{item.icon}</span>
            {isOpen && <span className="nav-label">{item.label}</span>}
          </Link>
        ))}
      </nav>

      <div className="sidebar-footer">
        {isOpen && user && <p className="user-info">👤 {user.username}</p>}
        <button className="logout-btn" onClick={logout} title="Logout">
          {isOpen ? '🚪 Logout' : '🚪'}
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
