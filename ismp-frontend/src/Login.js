import React, { useState } from 'react';
import { Box, Card, CardContent, TextField, Button, Typography, Alert } from '@mui/material';
import { LockOpen } from '@mui/icons-material';
import axios from 'axios';

const Login = ({ onLoginSuccess }) => {
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(""); // Clear previous errors

        try {
            const response = await axios.post("http://localhost:9090/api/auth/login", {
                username: credentials.username,
                password: credentials.password
            });

            console.log("Server Response:", response.data);

            // IMPORTANT: Extract the string from the JSON object { "token": "..." }
            // This matches your Spring Boot AuthController Map.of("token", token)
            const token = response.data.token;

            if (token) {
                localStorage.setItem("token", token);
                console.log("Token saved to LocalStorage successfully!");
                onLoginSuccess(); // This triggers App.js to switch views
            } else {
                setError("Login failed: Server did not return a valid token.");
            }
        } catch (err) {
            console.error("Login catch block error:", err);
            if (!err.response) {
                setError("Cannot connect to server. Is Spring Boot running on port 9090?");
            } else {
                setError("Invalid username or password.");
            }
        }
    };

    return (
        <Box sx={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', bgcolor: '#f0f2f5' }}>
            <Card sx={{ width: 400, boxShadow: 10, borderRadius: 3 }}>
                <CardContent sx={{ p: 4, textAlign: 'center' }}>
                    <LockOpen sx={{ fontSize: 50, color: '#1e3a8a', mb: 2 }} />
                    <Typography variant="h5" fontWeight="bold" gutterBottom>
                        ISMP Odisha Portal
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                        Integrated School Management Portal
                    </Typography>
                    
                    <form onSubmit={handleLogin}>
                        <TextField 
                            fullWidth 
                            label="Username" 
                            variant="outlined"
                            margin="normal"
                            required
                            onChange={(e) => setCredentials({...credentials, username: e.target.value})}
                        />
                        <TextField 
                            fullWidth 
                            label="Password" 
                            type="password" 
                            variant="outlined"
                            margin="normal"
                            required
                            onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                        />
                        
                        {error && (
                            <Alert severity="error" sx={{ mt: 2, textAlign: 'left' }}>
                                {error}
                            </Alert>
                        )}

                        <Button 
                            fullWidth 
                            type="submit" 
                            variant="contained" 
                            size="large"
                            sx={{ mt: 3, py: 1.5, bgcolor: '#1e3a8a', '&:hover': { bgcolor: '#152a61' } }}
                        >
                            Sign In
                        </Button>
                    </form>
                </CardContent>
            </Card>
        </Box>
    );
};

export default Login;