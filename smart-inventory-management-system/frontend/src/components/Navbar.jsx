import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import '../styles/Navbar.css';

const Navbar = () => {
  const { user, logout } = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);

  return (
    <nav className="navbar">
      <div className="navbar-content">
        <div className="navbar-left">
          <h1 className="navbar-title">Smart Inventory Management System</h1>
        </div>

        <div className="navbar-right">
          <div className="navbar-user">
            <button 
              className="user-btn"
              onClick={() => setShowDropdown(!showDropdown)}
            >
              <span className="user-icon">👤</span>
              {user?.username}
            </button>

            {showDropdown && (
              <div className="dropdown-menu">
                <div className="dropdown-header">
                  <strong>{user?.username}</strong>
                  <small>{user?.role}</small>
                </div>
                <hr />
                <button 
                  className="dropdown-item logout"
                  onClick={() => {
                    logout();
                    setShowDropdown(false);
                  }}
                >
                  🚪 Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
