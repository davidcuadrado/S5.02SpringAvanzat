// src/pages/RegistrationPage.js
import React, { useState } from 'react';
import axios from 'axios';

const RegistrationPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      // Sending a POST request to the backend to register a new user
      const response = await axios.post(`${process.env.REACT_APP_API_URL}/register/new`, {
        username,
        password,
      });

      // If registration is successful, inform the user
      if (response.status === 200 || response.status === 201) {
        setMessage('Registration successful! You can now log in.');
        setError('');
      }
    } catch (err) {
      // Capture any errors and display a user-friendly message
      setMessage('');
      setError('Registration failed. Please try again.');
      console.error('Registration error:', err.response || err.message);
    }
  };

  return (
    <div>
      <h1>Register</h1>
      <form onSubmit={handleRegister}>
        <div>
          <label>Username:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Register</button>
      </form>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default RegistrationPage;
