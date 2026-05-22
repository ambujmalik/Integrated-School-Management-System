import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { 
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, Typography, TextField, Button, Box, Card, CardContent 
} from '@mui/material';
import { AddCircleOutline } from '@mui/icons-material';

const OfficerList = () => {
    const [officers, setOfficers] = useState([]);
    const [formData, setFormData] = useState({ name: '', office: '', secretaryId: 1 });

    const fetchOfficers = () => {
        axios.get('http://localhost:9090/api/regional-officers')
            .then(res => setOfficers(res.data))
            .catch(err => console.error(err));
    };

    useEffect(() => { fetchOfficers(); }, []);

    const handleSubmit = (e) => {
        e.preventDefault();
        const payload = { name: formData.name, office: formData.office, secretary: { id: formData.secretaryId } };
        axios.post('http://localhost:9090/api/regional-officers', payload)
            .then(() => {
                setFormData({ name: '', office: '', secretaryId: 1 });
                fetchOfficers();
            });
    };

    return (
        <Box>
            <Typography variant="h4" gutterBottom sx={{ color: '#1e3a8a', fontWeight: 'bold' }}>
                Regional Officer Management
            </Typography>

            {/* Registration Form Card */}
            <Card sx={{ mb: 4, boxShadow: 3 }}>
                <CardContent>
                    <Typography variant="h6" gutterBottom>Register New Officer</Typography>
                    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
                        <TextField 
                            label="Officer Name" 
                            variant="outlined" 
                            size="small"
                            value={formData.name}
                            onChange={(e) => setFormData({...formData, name: e.target.value})}
                            required
                        />
                        <TextField 
                            label="Office / Zone" 
                            variant="outlined" 
                            size="small"
                            value={formData.office}
                            onChange={(e) => setFormData({...formData, office: e.target.value})}
                            required
                        />
                        <Button 
                            type="submit" 
                            variant="contained" 
                            startIcon={<AddCircleOutline />}
                            sx={{ backgroundColor: '#1e3a8a' }}
                        >
                            Add Officer
                        </Button>
                    </Box>
                </CardContent>
            </Card>

            {/* Data Table */}
            <TableContainer component={Paper} sx={{ boxShadow: 3 }}>
                <Table>
                    <TableHead sx={{ backgroundColor: '#f5f5f5' }}>
                        <TableRow>
                            <TableCell><strong>ID</strong></TableCell>
                            <TableCell><strong>Officer Name</strong></TableCell>
                            <TableCell><strong>Office / Jurisdiction</strong></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {officers.map((officer) => (
                            <TableRow key={officer.id} hover>
                                <TableCell>{officer.id}</TableCell>
                                <TableCell>{officer.name}</TableCell>
                                <TableCell>{officer.office}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default OfficerList;