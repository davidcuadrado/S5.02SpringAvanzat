import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav style={styles.navbar}>
      <ul style={styles.navList}>
        <li style={styles.navItem}>
          <Link to="/home" style={styles.navLink}>Home</Link>
        </li>
        <li style={styles.navItem}>
          <Link to="/home/login" style={styles.navLink}>Login</Link>
        </li>
        <li style={styles.navItem}>
          <Link to="/home/register" style={styles.navLink}>Register</Link>
        </li>
      </ul>
    </nav>
  );
};

const styles = {
  navbar: {
    position: 'fixed',
    top: 0,
    width: '100%',
    height: '60px', // Adjust this height as needed
    backgroundColor: '#333', // Navbar background color
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'left',
    zIndex: 1000, // Ensure it stays on top of other elements
    boxShadow: '0 2px 5px rgba(0,0,0,0.1)', // Optional shadow for better visuals
  },
  navList: {
    listStyleType: 'none',
    display: 'flex',
    margin: 0,
    padding: 0,
  },
  navItem: {
    margin: '0 15px',
  },
  navLink: {
    color: '#fff', // Link text color
    textDecoration: 'none',
    fontSize: '18px',
    fontWeight: 'bold',
    transition: 'color 0.3s',
  },
  navLinkHover: {
    color: '#ff9800', // Hover color
  },
};

export default Navbar;
