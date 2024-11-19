import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import LogoutButton from '../components/LogoutButton';

const AdminHomePage = () => {
  const [backgroundImage, setBackgroundImage] = useState('');

  useEffect(() => {
    const randomImageNumber = Math.floor(Math.random() * 16) + 1;
    const imagePath = `/images/resource/${randomImageNumber}.webp`;
    setBackgroundImage(imagePath);
  }, []);

  return (
    <div
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      {/* Top-right logout button */}
      <div style={logoutContainerStyle}>
        <LogoutButton />
      </div>

      {/* Centered content */}
      <div
        style={{
          backgroundColor: 'rgba(255, 223, 186, 0.8)', // Semi-transparent gold
          padding: '20px',
          borderRadius: '10px',
          textAlign: 'center',
          boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
          maxWidth: '600px',
          width: '100%',
          margin: 'auto',
          border: '2px solid gold', // Golden border for emphasis
        }}
      >
        <h1 style={{ color: '#333', marginBottom: '20px' }}>Admin Dashboard</h1>
        <p style={{ color: '#555', marginBottom: '20px' }}>
          Welcome, Admin! Manage pets, users, and more from here.
        </p>
        <div style={{ marginBottom: '20px' }}>
          <Link to="/user/create">
            <button style={buttonStyle}>Create new pet</button>
          </Link>
        </div>
        <div style={{ marginBottom: '20px' }}>
          <Link to="/user/read">
            <button style={buttonStyle}>Your pets are waiting for you!</button>
          </Link>
        </div>
        <div style={{ marginBottom: '20px' }}>
          <Link to="/admin/pets">
            <button style={buttonStyle}>Go to all the pets</button>
          </Link>
        </div>
        <div style={{ marginBottom: '20px' }}>
          <Link to="/admin/pet-management">
            <button style={buttonStyle}>Manage Pets (Site still building)</button>
          </Link>
        </div>
        <div style={{ marginBottom: '20px' }}>
          <Link to="/admin/user-management">
            <button style={buttonStyle}>Manage Users (Site still building)</button>
          </Link>
        </div>
      </div>
    </div>
  );
};

const logoutContainerStyle = {
  display: 'flex',
  justifyContent: 'flex-end',
  padding: '10px 20px',
  position: 'absolute',
  top: 10,
  right: 0,
  width: '100%',
  boxSizing: 'border-box',
  zIndex: 10000,
};

const buttonStyle = {
  padding: '10px 20px',
  fontSize: '16px',
  color: '#fff',
  backgroundColor: '#FFD700', // Golden button for admin
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
  width: '100%',
  marginTop: '10px',
};

export default AdminHomePage;
