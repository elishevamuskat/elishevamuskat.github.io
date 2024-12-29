import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import ProtectedRoute from './components/ProtectedRoute';
import HomePage from './pages/HomePage';
import AboutPage from './pages/AboutPage';
import AppToolbar from './components/Toolbar';
import GuestPage from "./pages/GuestPage"; // Import the new component


function App() {
  return (
    <Router>
      <AppToolbar />
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/home" element={
          <ProtectedRoute>
            <HomePage />
           </ProtectedRoute>
          }
        /> 
        <Route path="/guest/:username/:weddingName" element={
            <GuestPage />
          } />
        <Route path="/about" element={
          <ProtectedRoute> 
            <AboutPage />
          </ProtectedRoute>
          } 
        />
      </Routes>
    </Router>
  );
}

export default App;
