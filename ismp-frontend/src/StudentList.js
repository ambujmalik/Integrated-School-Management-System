import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { 
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, 
  Paper, Typography, TextField, Button, Box, Card, CardContent, Grid, MenuItem,
  IconButton, Tooltip, Alert, Divider
} from '@mui/material';
import { 
  PersonAdd, Delete, Edit, Search, Save, Cancel, CloudUpload, FilePresent 
} from '@mui/icons-material';

const StudentList = () => {
    const [students, setStudents] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [isEditing, setIsEditing] = useState(false);
    const [currentStudentId, setCurrentStudentId] = useState(null);

    // --- Bulk Upload States ---
    const [file, setFile] = useState(null);
    const [uploadStatus, setUploadStatus] = useState({ type: '', msg: '' });

    const [formData, setFormData] = useState({
        studentCode: '', admissionNumber: '', firstName: '', lastName: '',
        dateOfBirth: '', gender: '', admissionDate: new Date().toISOString().split('T')[0],
        academicYear: '2025-26', currentClass: ''
    });

    const fetchStudents = () => {
        axios.get('http://localhost:9090/api/students')
            .then(res => setStudents(Array.isArray(res.data) ? res.data : [res.data]))
            .catch(err => console.error("Fetch error:", err));
    };

    useEffect(() => { fetchStudents(); }, []);

    // --- BULK UPLOAD HANDLER ---
    const handleBulkUpload = () => {
        if (!file) return;
        const uploadData = new FormData();
        uploadData.append("file", file);

        axios.post("http://localhost:9090/api/students/upload", uploadData)
            .then(() => {
                setUploadStatus({ type: 'success', msg: "Bulk Upload Successful!" });
                setFile(null);
                fetchStudents();
                // Clear status after 5 seconds
                setTimeout(() => setUploadStatus({ type: '', msg: '' }), 5000);
            })
            .catch(err => {
                setUploadStatus({ type: 'error', msg: "Upload Failed. Check Excel format." });
            });
    };

    const handleEditClick = (student) => {
        setIsEditing(true);
        setCurrentStudentId(student.studentId);
        setFormData({ ...student });
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const resetForm = () => {
        setIsEditing(false);
        setCurrentStudentId(null);
        setFormData({
            studentCode: '', admissionNumber: '', firstName: '', lastName: '',
            dateOfBirth: '', gender: '', admissionDate: new Date().toISOString().split('T')[0],
            academicYear: '2025-26', currentClass: ''
        });
    };

    const handleDelete = (id) => {
        if (window.confirm("Delete this student?")) {
            axios.delete(`http://localhost:9090/api/students/${id}`)
                .then(() => { alert("Deleted!"); fetchStudents(); });
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const apiCall = isEditing 
            ? axios.put(`http://localhost:9090/api/students/${currentStudentId}`, formData)
            : axios.post('http://localhost:9090/api/students', formData);

        apiCall.then(() => {
            alert(isEditing ? "Updated!" : "Enrolled!");
            fetchStudents();
            resetForm();
        }).catch(err => console.error(err));
    };

    const filteredStudents = students.filter(s => 
        s.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        s.studentCode?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" gutterBottom sx={{ color: '#1e3a8a', fontWeight: 'bold' }}>
                Student Management
            </Typography>

            {/* --- 1. MANUAL ENROLLMENT FORM --- */}
            <Card sx={{ mb: 4, boxShadow: 3, borderLeft: isEditing ? '6px solid #ed6c02' : '6px solid #1e3a8a' }}>
                <CardContent>
                    <Typography variant="h6" gutterBottom>{isEditing ? "Update Student" : "New Admission"}</Typography>
                    <Box component="form" onSubmit={handleSubmit}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={3}><TextField fullWidth label="Code" size="small" value={formData.studentCode} onChange={(e) => setFormData({...formData, studentCode: e.target.value})} required /></Grid>
                            <Grid item xs={12} sm={3}><TextField fullWidth label="First Name" size="small" value={formData.firstName} onChange={(e) => setFormData({...formData, firstName: e.target.value})} required /></Grid>
                            <Grid item xs={12} sm={3}><TextField fullWidth label="Last Name" size="small" value={formData.lastName} onChange={(e) => setFormData({...formData, lastName: e.target.value})} required /></Grid>
                            <Grid item xs={12} sm={3}><TextField fullWidth label="Class" type="number" size="small" value={formData.currentClass} onChange={(e) => setFormData({...formData, currentClass: e.target.value})} required /></Grid>
                            <Grid item xs={12} sm={12} sx={{ textAlign: 'right' }}>
                                <Button type="submit" variant="contained" startIcon={isEditing ? <Save /> : <PersonAdd />} color={isEditing ? "warning" : "primary"}>
                                    {isEditing ? "Update" : "Enroll Student"}
                                </Button>
                                {isEditing && <Button onClick={resetForm} sx={{ ml: 1 }}>Cancel</Button>}
                            </Grid>
                        </Grid>
                    </Box>
                </CardContent>
            </Card>

            {/* --- 2. BULK UPLOAD SECTION --- */}
            <Card sx={{ mb: 4, bgcolor: '#f8fafc', border: '2px dashed #cbd5e1' }}>
                <CardContent sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', py: 3 }}>
                    <Typography variant="h6" sx={{ mb: 2, color: '#475569' }}>Bulk Student Import (Excel)</Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        <Button variant="outlined" component="label" startIcon={<FilePresent />}>
                            Select Excel File
                            <input type="file" hidden accept=".xlsx, .xls" onChange={(e) => setFile(e.target.files[0])} />
                        </Button>
                        {file && <Typography variant="body2">Selected: <b>{file.name}</b></Typography>}
                        <Button 
                            variant="contained" 
                            color="success" 
                            startIcon={<CloudUpload />} 
                           disabled={!file} 
                            onClick={handleBulkUpload}
                        >
                            Upload Now
                        </Button>
                    </Box>
                    {uploadStatus.msg && (
                        <Alert severity={uploadStatus.type} sx={{ mt: 2, width: '100%', maxWidth: '500px' }}>
                            {uploadStatus.msg}
                        </Alert>
                    )}
                </CardContent>
            </Card>

            {/* --- 3. SEARCH & TABLE --- */}
            <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="h6">Student Records</Typography>
                <TextField
                    placeholder="Search by name or code..."
                    size="small"
                    InputProps={{ startAdornment: <Search sx={{ mr: 1, color: 'gray' }} /> }}
                    sx={{ width: '300px', bgcolor: 'white' }}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </Box>

            <TableContainer component={Paper} sx={{ boxShadow: 3 }}>
                <Table>
                    <TableHead sx={{ bgcolor: '#f1f5f9' }}>
                        <TableRow>
                            <TableCell><b>Code</b></TableCell>
                            <TableCell><b>Full Name</b></TableCell>
                            <TableCell><b>Class</b></TableCell>
                            <TableCell align="center"><b>Actions</b></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {filteredStudents.map((s) => (
                            <TableRow key={s.studentId} hover>
                                <TableCell>{s.studentCode}</TableCell>
                                <TableCell>{`${s.firstName} ${s.lastName}`}</TableCell>
                                <TableCell>{s.currentClass}</TableCell>
                                <TableCell align="center">
                                    <IconButton color="primary" onClick={() => handleEditClick(s)}><Edit /></IconButton>
                                    <IconButton color="error" onClick={() => handleDelete(s.studentId)}><Delete /></IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default StudentList;