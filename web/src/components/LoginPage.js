import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { TextField, Button, Alert, Container, Paper, Typography, Box } from '@mui/material';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [registerSuccess, setRegisterSuccess] = useState(''); // For successful registration message
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      console.log("CONSOLE WORKING");
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password,
      });
      console.log("GOT TOKEN", response);
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('username', username)
      console.log("STORED TOKEN");
      axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
      console.log("GOT TO NAVIGATE HOME");
      navigate('/home');
    } catch (err) {
      setError('Invalid credentials');
    }
  };

  const handleRegister = async () => {
    try {
      const response = await axios.post(`http://localhost:8080/api/auth/register/member/${username}/${password}`, {
        username: "admin",
        password: "admin123"
      });
      console.log(response.data.token)
      localStorage.setItem('token',response.data.token)
      setRegisterSuccess('Registration successful! You can now log in.');
      setError('');
    } catch (err) {
      setError('Registration failed. Please try again.');
    }
  };

  return (
    <Container maxWidth="xs">
      <Paper elevation={3} sx={{ padding: 3 }}>
        <Typography variant="h5">Login</Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {registerSuccess && <Alert severity="success">{registerSuccess}</Alert>}
        <Box component="form" onSubmit={handleLogin}>
          <TextField
            label="Username"
            fullWidth
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            margin="normal"
          />
          <TextField
            label="Password"
            type="password"
            fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            margin="normal"
          />
          <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
            Login
          </Button>
        </Box>
        <Typography align="center" sx={{ mt: 2 }}>
          — or —
        </Typography>
        <Button
          variant="outlined"
          fullWidth
          sx={{ mt: 2 }}
          onClick={handleRegister}
          disabled={!username || !password} // Ensure fields are filled
        >
          Register New User
        </Button>
      </Paper>
    </Container>
  );
}

export default LoginPage;
