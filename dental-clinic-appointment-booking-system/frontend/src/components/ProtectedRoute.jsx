import React, { useContext } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const ProtectedRoute = ({ allowedRoles }) => {
  const { auth } = useContext(AuthContext);

  if (!auth) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(auth.role)) {
    // Redirect to correct dashboard if role mismatch
    return auth.role === 'DOCTOR' ? (
      <Navigate to="/doctor/dashboard" replace />
    ) : (
      <Navigate to="/patient/dashboard" replace />
    );
  }

  return <Outlet />;
};

export default ProtectedRoute;
