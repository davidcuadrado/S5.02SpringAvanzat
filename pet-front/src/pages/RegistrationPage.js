import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const RegistrationPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isRegistered, setIsRegistered] = useState(false);
  const [backgroundImage, setBackgroundImage] = useState('');

  useEffect(() => {
    const randomImageNumber = Math.floor(Math.random() * 16) + 1;
    const imagePath = `/images/resource/${randomImageNumber}.webp`;
    setBackgroundImage(imagePath);
  }, []);

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`${process.env.REACT_APP_API_URL}/register/new`, {
        username,
        password,
        role: 'USER',
      });

      if (response.status === 200) {
        setMessage('Registration successful! You can now log in.');
        setIsRegistered(true);
      }
    } catch (error) {
      console.error('Registration error:', error.response || error.message);
      setMessage('Registration failed. Try again.');
    }
  };

  return (
    <div
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '100vh',
        display: 'flex',
        alignItems: 'flex-start',
        justifyContent: 'center',
        paddingTop: '60px', // Space from the top
      }}
    >
      <div
        style={{
          backgroundColor: 'rgba(255, 255, 255, 0.9)', // Slightly more opaque for better readability
          padding: '30px',
          borderRadius: '10px',
          textAlign: 'center',
          boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
          maxWidth: '400px',
          width: '100%',
          marginTop: '90px', // Move the panel slightly up
        }}
      >
        <h1 style={{ color: '#333', marginBottom: '20px' }}>Register</h1>
        <form onSubmit={handleRegister}>
          <div style={{ marginBottom: '20px' }}>
            <label htmlFor="username" style={labelStyle}>Username:</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              style={inputStyle}
            />
          </div>
          <div style={{ marginBottom: '20px' }}>
            <label htmlFor="password" style={labelStyle}>Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              style={inputStyle}
            />
          </div>
          <button type="submit" style={buttonStyle}>Register</button>
        </form>
        {message && (
          <p style={{ marginTop: '15px', color: isRegistered ? 'green' : 'red' }}>
            {isRegistered ? (
              <>
                {message} <Link to="/home/login" style={{ color: '#3D3D3D' }}>Login</Link>.
              </>
            ) : (
              message
            )}
          </p>
        )}
      </div>
    </div>
  );
};

const buttonStyle = {
  padding: '10px 20px',
  fontSize: '16px',
  color: '#fff',
  backgroundColor: '#3D3D3D',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
  width: '100%',
  marginTop: '15px',
};

const inputStyle = {
  width: '100%',
  padding: '10px',
  fontSize: '16px',
  border: '1px solid #ccc',
  borderRadius: '5px',
  marginTop: '5px',
};

const labelStyle = {
  display: 'block',
  fontSize: '14px',
  fontWeight: 'bold',
  color: '#333',
  marginBottom: '5px',
};

export default RegistrationPage;