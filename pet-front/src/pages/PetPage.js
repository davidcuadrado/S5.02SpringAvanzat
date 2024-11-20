import React, { useEffect, useState, useCallback } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from '../axiosConfig';
import LogoutButton from '../components/LogoutButton';

const PetPage = () => {
  const { id } = useParams(); // Get pet ID from the route parameter
  const [backgroundImage, setBackgroundImage] = useState('');
  const [pet, setPet] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Feedback message for action results
  const [actionFeedback, setActionFeedback] = useState('');
  const [accessoryImage, setAccessoryImage] = useState(''); // New state for accessory image


  const fetchPet = useCallback(async () => {
    setLoading(true);
    try {
      const response = await axios.get(`${process.env.REACT_APP_API_URL}/user/${id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
      console.log('Pet fetched successfully:', response.data);
      setPet(response.data);

      // Set the background image based on pet environment
      if (response.data.environment) {
        const environmentImagePath = `/images/environment/${response.data.environment.toLowerCase()}.webp`;
        setBackgroundImage(environmentImagePath);
      }
      setError(null); // Clear any existing errors
    } catch (err) {
      if (err.response) {
        console.error('Error fetching pet:', err.response.status, err.response.data);
        setError(`Failed to load pet details. Server responded with: ${err.response.status}`);
      } else {
        console.error('Network error or server not responding:', err.message);
        setError('Failed to load pet details. Please check your connection.');
      }
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    fetchPet();
  }, [fetchPet]);

  const handlePetAction = async (action) => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/user/${id}/update`,
        action, // Send action as a string in the request body
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'text/plain',
          },
        }
      );
      console.log(`Action ${action} completed successfully:`, response.data);
      setActionFeedback(`Action ${action} completed successfully!`);

      // Refresh the pet data after performing the action
      await fetchPet();
    } catch (err) {
      if (err.response) {
        console.error(`Error performing action ${action}:`, err.response.status, err.response.data);
        setActionFeedback(
          `Failed to perform action ${action}. Server responded with: ${err.response.data || 'Unknown error.'}`
        );
      } else {
        console.error(`Network error during action ${action}:`, err.message);
        setActionFeedback(`Failed to perform action ${action}. Please check your connection.`);
      }
    }
  };

  const displayAccessory = () => {
    if (pet?.specialTraits?.length) {
      const randomAccessory = pet.specialTraits[Math.floor(Math.random() * pet.specialTraits.length)];
      setAccessoryImage(`/images/accessories/${randomAccessory.toLowerCase()}.webp`);
    } else {
      setAccessoryImage(''); // Clear accessory image if no traits exist
      setActionFeedback('No accessories available for this pet.');
    }
  };

  const actions = [
    'feed',
    'play',
    'environment',
    'cheer',
    'accessory',
    'sleep',
    'clean',
    'adventure',
    'check',
  ];

  const getPetImage = (color, mood, petType) => `/images//animal/${petType?.toLowerCase()}/${color?.toLowerCase()}/${mood?.toLowerCase()}.webp`;

  return (
    <div className="background-container" style={{ backgroundImage: `url(${backgroundImage})` }}>
      {/* Top-right logout button */}
      <div style={logoutContainerStyle}>
        <LogoutButton />
      </div>

      {/* Error or Feedback Message */}
      {(error || actionFeedback) && (
        <div style={messageCardStyle}>
          <p>{error || actionFeedback}</p>
        </div>
      )}

      {/* Centered content */}
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100%',
        }}
      >
        {loading && <p>Loading pet details...</p>}
        {!loading && !error && pet && (
          <div style={petPageLayoutStyle}>
            {/* Pet Image */}
            <div style={petImageContainerStyle}>
              <img
                src={getPetImage(pet.petColor, pet.currentMood, pet.petType)}
                alt={`${pet.petColor} ${pet.mood} ${pet.petType} pet`}
                style={petTypeImageStyle}
                onError={(e) => {
                  // Fallback if image is not found
                  e.target.src = `/images/animal/${pet.petType?.toLowerCase()}.webp`;
                }}
              />
              {/* Accessory Image */}
              {accessoryImage && (
                <img
                  src={accessoryImage}
                  alt="Pet Accessory"
                  style={accessoryImageStyle}
                  onError={(e) => {
                    // Fallback for missing accessory images
                    e.target.src = '/images/accessories/default.webp';
                  }}
                />
              )}
            </div>

            {/* Pet Details */}
            <div style={detailsCardStyle}>
              <h2>{pet.name}</h2>
              <p><strong>Type:</strong> {pet.petType}</p>
              <p><strong>Color:</strong> {pet.petColor}</p>
              <p><strong>Happiness:</strong> {pet.happiness}</p>
              <p><strong>Energy:</strong> {pet.energy}</p>
              <p><strong>Hunger:</strong> {pet.hunger}</p>
              <p><strong>Hygiene:</strong> {pet.hygiene}</p>
              <p><strong>Health:</strong> {pet.health}</p>
              <p><strong>Environment:</strong> {pet.environment}</p>
              <p><strong>Mood:</strong> {pet.mood}</p>

              {/* Action Buttons */}
              <div style={{ marginTop: '20px' }}>
                {actions.map((action) => (
                  <button
                    key={action}
                    style={buttonStyle}
                    onClick={() => handlePetAction(action)}
                  >
                    {action.charAt(0).toUpperCase() + action.slice(1)}
                  </button>
                ))}
              </div>
            </div>
          </div>
        )}
        {!loading && !error && !pet && (
          <p>Pet not found. Please go back to the dashboard.</p>
        )}
      </div>

      {/* Navigation Buttons */}
      <div style={navigationButtonsContainerStyle}>
        <Link to="/user/home">
          <button style={navigationButtonStyle}>Return to user home</button>
        </Link>
        <Link to="/user/read">
          <button style={navigationButtonStyle}>Return to pet list</button>
        </Link>
      </div>

      {/* Background Animation CSS */}
      <style>
        {`
          .background-container {
            background-size: cover;
            background-position: center;
            height: 100vh;
            display: flex;
            flex-direction: column;
            animation: backgroundMove 60s linear infinite;
          }

          @keyframes backgroundMove {
            0% { background-position: center top; }
            50% { background-position: center bottom; }
            100% { background-position: center top; }
          }
        `}
      </style>
    </div>
  );
};

// Styles
const accessoryImageStyle = {
  width: '100px',
  height: '100px',
  marginTop: '10px',
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

const messageCardStyle = {
  position: 'absolute',
  top: '80px',
  left: '50%',
  transform: 'translateX(-50%)',
  backgroundColor: 'rgba(255, 0, 0, 0.8)',
  color: '#fff',
  padding: '20px',
  borderRadius: '10px',
  boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
  maxWidth: '600px',
  width: '90%',
  textAlign: 'center',
  zIndex: 1000,
};

const petPageLayoutStyle = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
  width: '80%',
  maxWidth: '1200px',
};

const petImageContainerStyle = {
  flex: '1',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
};

const petTypeImageStyle = {
  width: '400px',
  height: '400px',
  marginRight: '20px',
  objectFit: 'cover',
};

const detailsCardStyle = {
  flex: '1',
  backgroundColor: 'rgba(255, 255, 255, 0.8)',
  padding: '30px',
  borderRadius: '10px',
  boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
  textAlign: 'left',
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
  marginTop: '10px',
  marginRight: '10px',
};

const navigationButtonsContainerStyle = {
  display: 'flex',
  justifyContent: 'center',
  marginTop: '20px',
  gap: '20px',
};

const navigationButtonStyle = {
  padding: '10px 20px',
  fontSize: '16px',
  color: '#fff',
  backgroundColor: '#3D3D3D',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
};

export default PetPage;
