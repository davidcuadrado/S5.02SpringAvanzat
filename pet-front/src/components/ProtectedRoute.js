import React from "react";
import { Navigate } from "react-router-dom";

/**
 * Role-based ProtectedRoute component
 * @param {Object} props
 * @param {React.ReactNode} props.children - The component/page to render if access is allowed
 * @param {string[]} props.roles - Array of roles allowed to access the route
 */
const ProtectedRoute = ({ children, roles }) => {
  const userRole = sessionStorage.getItem('role');
  console.log('Current Role:', userRole);

  if (!userRole) {
    console.log('No role found. Redirecting to login.');
    return <Navigate to="/home/login" />;
  }

  if (roles && !roles.includes(userRole)) {
    console.log(`Unauthorized for role: ${userRole}`);
    return <Navigate to="/unauthorized" />;
  }

  console.log('Authorized. Rendering child component.');
  return children;
};


export default ProtectedRoute;
