import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

/**
 * BUG-17 FIX: Check if a JWT token is expired by decoding its payload.
 * Returns true if the token is expired or invalid.
 */
function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    // exp is in seconds; Date.now() is in milliseconds
    return payload.exp * 1000 < Date.now();
  } catch {
    // If decoding fails, treat the token as expired/invalid
    return true;
  }
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    const role = localStorage.getItem('role');
    const userId = localStorage.getItem('userId');

    // BUG-17 FIX: Validate token expiry on page load.
    // Previously, expired tokens were silently accepted, making the user appear
    // logged in until their first API call triggered a jarring 401 redirect.
    if (token && username && role && !isTokenExpired(token)) {
      setUser({ token, username, role, userId: userId ? Number(userId) : null });
    } else if (token) {
      // Token present but expired — clear stale auth data
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
      localStorage.removeItem('userId');
    }
    setLoading(false);
  }, []);

  const login = (authData) => {
    localStorage.setItem('token', authData.token);
    localStorage.setItem('username', authData.username);
    localStorage.setItem('role', authData.role);
    if (authData.userId) localStorage.setItem('userId', authData.userId);
    setUser(authData);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider');
  return ctx;
};
