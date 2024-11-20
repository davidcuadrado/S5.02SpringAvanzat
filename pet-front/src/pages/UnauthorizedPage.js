import React, { useEffect, useState } from 'react';

const UnauthorizedPage = () => {
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
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <div
        style={{
          backgroundColor: 'rgba(255, 255, 255, 0.8)', // Semi-transparent white
          padding: '20px',
          borderRadius: '10px',
          textAlign: 'center',
          boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
          maxWidth: '400px',
          width: '100%',
        }}
      >
        <h1 style={{ color: '#333', marginBottom: '20px' }}>401 - Unauthorized</h1>
        <p style={{ color: '#555', marginBottom: '20px' }}>
          You do not have permission to access this page.
        </p>
        <a
          href="/home"
          style={{
            textDecoration: 'none',
            color: '#fff',
            backgroundColor: '#3D3D3D',
            padding: '10px 20px',
            borderRadius: '5px',
            transition: 'background-color 0.3s',
            display: 'inline-block',
          }}
        >
          Go to Home
        </a>
      </div>
    </div>
  );
};

export default UnauthorizedPage;
