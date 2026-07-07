import React, { useEffect } from 'react';
import '../styles/Modal.css';

/**
 * Reusable Modal component
 * 
 * Props:
 *   isOpen: boolean
 *   onClose: () => void
 *   title: string
 *   children: ReactNode
 *   size?: 'sm' | 'md' | 'lg' (default 'md')
 */
const Modal = ({ isOpen, onClose, title, children, size = 'md' }) => {
  useEffect(() => {
    if (!isOpen) return;
    
    const handleKey = (e) => {
      if (e.key === 'Escape') onClose();
    };
    
    document.addEventListener('keydown', handleKey);
    document.body.style.overflow = 'hidden';
    
    return () => {
      document.removeEventListener('keydown', handleKey);
      document.body.style.overflow = '';
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  const widths = {
    sm: '400px',
    md: '600px',
    lg: '800px'
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div 
        className="modal"
        style={{ maxWidth: widths[size] }}
        onClick={(e) => e.stopPropagation()}
      >
        <div className="modal-header">
          <h2 className="modal-title">{title}</h2>
          <button className="modal-close" onClick={onClose}>×</button>
        </div>
        <div className="modal-body">
          {children}
        </div>
      </div>
    </div>
  );
};

export default Modal;
