import React from 'react';
import { useNavigate } from 'react-router-dom';

const LogoutButton = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    sessionStorage.removeItem('token'); // Clear token
    sessionStorage.removeItem('role')
    navigate('/'); // Redirect to login
  };

  return <button onClick={handleLogout}>Logout</button>;
};

export default LogoutButton;
