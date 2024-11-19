import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import LogoutButton from '../components/LogoutButton';

const HomePage = ({ isLoggedIn }) => {
  const [backgroundImage, setBackgroundImage] = useState('');

  useEffect(() => {
    // Generate a random number between 1 and 16
    const randomImageNumber = Math.floor(Math.random() * 16) + 1;

    // Set the image path (assuming images are in the public/images folder)
    const imagePath = `/images/resource/${randomImageNumber}.webp`;

    // Update the background image state
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
      {/* Top-right logout button if logged in */}
      {isLoggedIn && (
        <div style={logoutContainerStyle}>
          <LogoutButton />
        </div>
      )}

      {/* Centered welcome content */}
      <div
        style={{
          backgroundColor: 'rgba(255, 255, 255, 0.8)', // Semi-transparent white
          padding: '20px',
          borderRadius: '10px',
          textAlign: 'center',
          boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
          maxWidth: '600px',
          width: '100%',
          margin: 'auto',
        }}
      >
        <h1 style={{ color: '#333' }}>Welcome to Petopia!</h1>
        <p style={{ color: '#555' }}>
          Your one-stop platform for managing and interacting with your pets.
        </p>
        {!isLoggedIn && (
          <div>
            <Link to="/home/login">
              <button style={buttonStyle}>Login</button>
            </Link>
            <Link to="/home/register">
              <button style={buttonStyle}>Register</button>
            </Link>
          </div>
        )}
      </div>
    </div>
  );
};

const logoutContainerStyle = {
  display: 'inline-block',
  justifyContent: 'flex-end',
  padding: '10px 20px',
  position: 'absolute',
  top: 10,
  right: -1820,
  width: '100%',
  boxSizing: 'border-box',
  zIndex: 10000,
};

const buttonStyle = {
  margin: '10px',
  padding: '10px 20px',
  fontSize: '16px',
  color: '#fff',
  backgroundColor: '#3D3D3D', // Carbon grey background color
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
};

buttonStyle[':hover'] = {
  backgroundColor: '#5A5A5A', // Slightly lighter grey for hover effect
};

export default HomePage;
