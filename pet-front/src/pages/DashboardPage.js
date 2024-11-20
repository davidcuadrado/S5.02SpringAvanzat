import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import LogoutButton from '../components/LogoutButton';

const DashboardPage = () => {
  const [backgroundImage, setBackgroundImage] = useState('');
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Set a random background image
    const randomImageNumber = Math.floor(Math.random() * 16) + 1;
    const imagePath = `/images/resource/${randomImageNumber}.webp`;
    setBackgroundImage(imagePath);

    // Fetch user's pets
    const fetchPets = async () => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/user/read`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        });
        setPets(response.data);
      } catch (err) {
        setError('Failed to load pets. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchPets();
  }, []);

  const getEnvironmentImage = (environment) => `/images/environment/${environment}.webp`;
  const getPetTypeImage = (type) => `/images/animal/${type.toLowerCase()}.webp`;

  const deletePet = async (petId) => {
    try {
      const response = await axios.delete(
        `${process.env.REACT_APP_API_URL}/user/${petId}/delete`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        }
      );
      if (response.status === 204) {
        setPets(pets.filter((pet) => pet.petId !== petId)); // Update the pet list
        alert(`Pet ${petId} deleted successfully.`);
      }
    } catch (err) {
      alert(
        err.response?.data || 'An error occurred while trying to delete the pet. Please try again.'
      );
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
          backgroundColor: 'rgba(255, 255, 255, 0.8)', // Semi-transparent white
          padding: '60px',
          borderRadius: '10px',
          textAlign: 'center',
          boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
          maxWidth: '600px',
          width: '100%',
          margin: 'auto',
          overflow: 'auto',
        }}
      >
        <h1 style={{ color: '#333', marginBottom: '20px' }}>Your Pets</h1>

        {/* Display loading, error, or pets */}
        {loading && <p>Loading pets...</p>}
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {!loading && !error && pets.length > 0 && (
          <ul style={petsListStyle}>
            {pets.map((pet) => (
              <li key={pet.petId} style={petItemStyle}>
                <Link to={`/user/${pet.petId}`} style={petLinkStyle}>
                  <div
                    style={{
                      ...petCardStyle,
                      backgroundImage: `url(${getEnvironmentImage(pet.environment)})`,
                      backgroundSize: 'cover',
                      backgroundPosition: 'center',
                      color: '#fff',
                      textShadow: '0 2px 4px rgba(0, 0, 0, 0.8)',
                    }}
                  >
                    <img
                      src={getPetTypeImage(pet.petType)}
                      alt={`${pet.petType}`}
                      style={petTypeImageStyle}
                    />
                    <h2>{pet.name}</h2>
                    <p>Type: {pet.petType}</p>
                  </div>
                </Link>
                <button
                  onClick={() => deletePet(pet.petId)}
                  style={deleteButtonStyle}
                >
                  Delete
                </button>
              </li>
            ))}
          </ul>
        )}
        {!loading && !error && pets.length === 0 && (
          <p>You don’t have any pets yet. Click below to add a pet.</p>
        )}

        <div style={{ marginTop: '20px' }}>
          <Link to="/user/create">
            <button style={buttonStyle}>Add a New Pet</button>
          </Link>
        </div>
      </div>
    </div>
  );
};

// Styles
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

const petsListStyle = {
  listStyleType: 'none',
  padding: 0,
  margin: '20px 0',
  textAlign: 'left',
};

const petItemStyle = {
  marginBottom: '15px',
};

const petCardStyle = {
  padding: '15px',
  borderRadius: '8px',
  boxShadow: '0 2px 5px rgba(0, 0, 0, 0.1)',
  height: '200px',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'flex-start',
  alignItems: 'center',
};

const petLinkStyle = {
  textDecoration: 'none',
  color: 'inherit',
};

const petTypeImageStyle = {
  width: '80px',
  height: '80px',
  marginBottom: '10px',
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
  marginTop: '10px',
};

const deleteButtonStyle = {
  marginTop: '10px',
  padding: '8px 16px',
  fontSize: '14px',
  color: '#fff',
  backgroundColor: '#e74c3c',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
};

export default DashboardPage;
