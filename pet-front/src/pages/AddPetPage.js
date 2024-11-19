import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const AddPetPage = () => {
  const [name, setName] = useState('');
  const [petType, setPetType] = useState('');
  const [petColor, setPetColor] = useState('');
  const [specialTreats, setSpecialTreats] = useState([]);
  const [petEnvironment, setEnvironment] = useState('');
  const [message, setMessage] = useState('');
  const [backgroundImage, setBackgroundImage] = useState('');
  const navigate = useNavigate();
  

  // Random background image logic
  useEffect(() => {
    const randomImageNumber = Math.floor(Math.random() * 16) + 1;
    const imagePath = `/images/resource/${randomImageNumber}.webp`;
    setBackgroundImage(imagePath);
  }, []);

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = sessionStorage.getItem('token');
    if (!token) {
      setMessage('Authorization token is missing. Please log in again.');
      return;
    }

    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/user/create`,
        {
          name,
          petType,
          petColor,
          specialTreats: specialTreats.length ? specialTreats : undefined,
          environment: petEnvironment,
          happiness: 80,
          energy: 80,
          hunger: 50,
          hygiene: 80,
          health: 80,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 201) {
        setMessage('Pet added successfully!');
        setTimeout(() => {
          navigate('/user/home'); // Redirect to /user/home after a success message
        }, 1500); // Optional: delay the redirection for UX purposes
      }
    } catch (error) {
      setMessage('Failed to add pet. Please try again.');
      console.error('Error:', error);
    }
  };

  // Handle adding a new treat
  const handleAddTreat = () => {
    setSpecialTreats([...specialTreats, '']);
  };

  // Handle updating a treat
  const handleTreatChange = (index, value) => {
    const treats = [...specialTreats];
    treats[index] = value;
    setSpecialTreats(treats);
  };

  return (
    <div
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '160vh',
        display: 'flex',
        alignItems: 'flex-start',
        justifyContent: 'center',
        paddingTop: '40px',
      }}
    >
      <div
        style={{
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          padding: '30px',
          borderRadius: '10px',
          textAlign: 'center',
          boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
          maxWidth: '500px',
          width: '100%',
          marginTop: '90px',
        }}
      >
        <h1 style={{ color: '#333', marginBottom: '20px' }}>Add a New Pet</h1>
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '20px' }}>
            <label style={labelStyle}>Name:</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              style={inputStyle}
            />
          </div>
          <div style={{ marginBottom: '20px' }}>
            <label style={labelStyle}>Type:</label>
            <select
              value={petType}
              onChange={(e) => setPetType(e.target.value)}
              required
              style={inputStyle}
            >
              <option value="">Select a pet type</option>
              <option value="DOG">Dog</option>
              <option value="CAT">Cat</option>
              <option value="DRAGON">Dragon</option>
              <option value="ALIEN">Alien</option>
              <option value="UNICORN">Unicorn</option>
              <option value="ALICORN">Alicorn</option>
              <option value="PEGASUS">Pegasus</option>
              <option value="BEAR">Bear</option>
              <option value="VAMPIRE">Vampire</option>
            </select>
          </div>
          <div style={{ marginBottom: '20px' }}>
            <label style={labelStyle}>Color:</label>
            <select
              value={petColor}
              onChange={(e) => setPetColor(e.target.value)}
              required
              style={inputStyle}
            >
              <option value="">Select a color</option>
              <option value="RED">Red</option>
              <option value="YELLOW">Yellow</option>
              <option value="BLUE">Blue</option>
            </select>
          </div>
          <div style={{ marginBottom: '20px' }}>
            <label style={labelStyle}>Environment:</label>
            <select
              value={petEnvironment}
              onChange={(e) => setEnvironment(e.target.value)}
              required
              style={inputStyle}
            >
              <option value="">Select a pet environment</option>
              <option value="URBAN">Urban</option>
              <option value="CITY">City</option>
              <option value="VILLAGE">Village</option>
              <option value="COUNTRY">Country</option>
              <option value="FOREST">Forest</option>
              <option value="JUNGLE">Jungle</option>
              <option value="MOUNTAIN">Mountain</option>
              <option value="HILLS">Hills</option>
              <option value="DESERT">Desert</option>
              <option value="POLE">Pole</option>
              <option value="SEA">Sea</option>
              <option value="OCEAN">Ocean</option>
            </select>
          </div>
          <div style={{ marginBottom: '20px' }}>
            <label style={labelStyle}>Special Treats:</label>
            {specialTreats.map((treat, index) => (
              <input
                key={index}
                value={treat}
                onChange={(e) => handleTreatChange(index, e.target.value)}
                placeholder="Add a treat"
                style={{ ...inputStyle, marginTop: '5px' }}
              />
            ))}
            <button
              type="button"
              onClick={handleAddTreat}
              style={{ ...buttonStyle, marginTop: '10px' }}
            >
              Add Treat
            </button>
          </div>
          <button type="submit" style={buttonStyle}>
            Add Pet
          </button>
        </form>
        {message && <p style={{ color: '#333', marginTop: '15px' }}>{message}</p>}
      </div>
    </div>
  );
};

// Styles
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

export default AddPetPage;
