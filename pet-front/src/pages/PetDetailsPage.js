import React, { useEffect, useState } from 'react';
import axios from 'axios';
import LogoutButton from '../components/LogoutButton';

const PetDetailsPage = () => {
  const [backgroundImage, setBackgroundImage] = useState('');
  const [pets, setPets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const randomImageNumber = Math.floor(Math.random() * 16) + 1;
    const imagePath = `/images/resource/${randomImageNumber}.webp`;
    setBackgroundImage(imagePath);

    const fetchPets = async () => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/admin/pets`, {
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

  const deletePet = async (petId) => {
    try {
      const response = await axios.delete(`${process.env.REACT_APP_API_URL}/admin/pets/${petId}/delete`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (response.status === 204) {
        setPets((prevPets) => prevPets.filter((pet) => pet.petId !== petId));
      } else {
        setError(response.data || 'Failed to delete pet. Please try again later.');
      }
    } catch (err) {
      setError('Failed to delete pet. Please try again later.');
    }
  };

  const getEnvironmentImage = (environment) => `/images/environment/${environment}.webp`;
  const getPetTypeImage = (type) => `/images/animal/${type.toLowerCase()}.webp`;

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
      <div style={logoutContainerStyle}>
        <LogoutButton />
      </div>

      <div
        style={{
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          padding: '30px',
          borderRadius: '10px',
          margin: 'auto',
          boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
          maxWidth: '1200px',
          overflowY: 'auto',
          height: '80%',
        }}
      >
        <h1 style={{ color: '#333', marginBottom: '20px', textAlign: 'center' }}>All Pets</h1>

        {loading && <p>Loading pets...</p>}
        {error && <p style={{ color: 'red' }}>{error}</p>}
        {!loading && !error && pets.length > 0 && (
          <div style={petsGridStyle}>
            {pets.map((pet) => (
              <div key={pet.petId} style={petCardStyle}>
                <div
                  style={{
                    ...petCardBackground,
                    backgroundImage: `url(${getEnvironmentImage(pet.environment)})`,
                  }}
                >
                  <img
                    src={getPetTypeImage(pet.petType)}
                    alt={pet.petType}
                    style={petTypeImageStyle}
                  />
                </div>
                <h2>{pet.name}</h2>
                <p>Type: {pet.petType}</p>
                <p>Environment: {pet.environment}</p>
                <p>User ID: {pet.userId}</p>
                <button
                  style={deleteButtonStyle}
                  onClick={() => {
                    if (window.confirm(`Are you sure you want to delete ${pet.name}?`)) {
                      deletePet(pet.petId);
                    }
                  }}
                >
                  Delete Pet
                </button>
              </div>
            ))}
          </div>
        )}
        {!loading && !error && pets.length === 0 && (
          <p>No pets available at the moment.</p>
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

const petsGridStyle = {
  display: 'grid',
  gridTemplateColumns: 'repeat(6, 1fr)',
  gap: '20px',
};

const petCardStyle = {
  backgroundColor: '#fff',
  borderRadius: '10px',
  boxShadow: '0 2px 5px rgba(0, 0, 0, 0.1)',
  padding: '15px',
  textAlign: 'center',
};

const petCardBackground = {
  borderRadius: '10px',
  height: '150px',
  width: '100%',
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  marginBottom: '10px',
};

const petTypeImageStyle = {
  width: '50px',
  height: '50px',
};

const deleteButtonStyle = {
  backgroundColor: '#ff4d4f',
  color: '#fff',
  border: 'none',
  borderRadius: '5px',
  padding: '10px 15px',
  cursor: 'pointer',
  marginTop: '10px',
};

export default PetDetailsPage;
