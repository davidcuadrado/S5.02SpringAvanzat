import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import PetDetailsPage from './pages/PetDetailsPage';
import ProtectedRoute from './components/ProtectedRoute';
import AddPetPage from './pages/AddPetPage';
import RegistrationPage from './pages/RegistrationPage';
import Navbar from './components/Navbar';
import UserHomePage from './pages/UserHomePage';
import AdminHomePage from './pages/AdminHomePage.js';
import UnauthorizedPage from './pages/UnauthorizedPage.js';
import PetPage from './pages/PetPage.js';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="" element={<HomePage />} /> {/* Default home page */}
        <Route path="/home" element={<HomePage />} /> {/* Default home page */}
        <Route path="/home/login" element={<LoginPage />} /> {/* Login page */}
        <Route path="/home/register" element={<RegistrationPage />} /> {/* Registration page */}
        <Route path="/unauthorized" element={<UnauthorizedPage />} />

        {/* Protected routes */}
        <Route
          path="/user/home"
          element={
            <ProtectedRoute roles={['ROLE_USER']}>
              <UserHomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/user/read"
          element={
            <ProtectedRoute roles={['ROLE_USER', 'ROLE_ADMIN']}>
              <DashboardPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/user/:id"
          element={
            <ProtectedRoute roles={['ROLE_USER', 'ROLE_ADMIN']}>
              <PetPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/user/create"
          element={
            <ProtectedRoute roles={['ROLE_USER', 'ROLE_ADMIN']}>
              <AddPetPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/home"
          element={
            <ProtectedRoute roles={['ROLE_ADMIN']}>
              <AdminHomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/pets"
          element={
            <ProtectedRoute roles={['ROLE_ADMIN']}>
              <PetDetailsPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
