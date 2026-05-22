import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { 
  Box, Typography, Table, TableBody, TableCell, TableContainer, 
  TableHead, TableRow, Paper, MenuItem, TextField, Button, Switch, 
  FormControlLabel, Grid, Card 
} from '@mui/material';
import { CheckCircle, Cancel, Group } from '@mui/icons-material';

const AttendanceMarker = () => {
    const [students, setStudents] = useState([]);
    const [classList, setClassList] = useState([]); 
    const [selectedClass, setSelectedClass] = useState('');
    const [attendanceDate, setAttendanceDate] = useState(new Date().toISOString().split('T')[0]);
    const [attendanceData, setAttendanceData] = useState({});
    const [loading, setLoading] = useState(false);

    // 1. Fetch available classes from DB
    useEffect(() => {
        axios.get('http://localhost:9090/api/classes') 
            .then(res => setClassList(Array.isArray(res.data) ? res.data : []))
            .catch(err => console.error("Error fetching classes:", err));
    }, []);

    // 2. Fetch students when class or date changes
    useEffect(() => {
        const fetchAttendance = async () => {
            if (!selectedClass) return;
            
            setLoading(true);
            try {
                // Step A: Get Students for the class
                console.log(`Fetching students for Class ID: ${selectedClass}`);
                const studentRes = await axios.get(`http://localhost:9090/api/students/class/${selectedClass}`);
                const studentList = Array.isArray(studentRes.data) ? studentRes.data : [];
                
                if (studentList.length === 0) {
                    console.warn("No students returned from server for this class ID.");
                }
                
                setStudents(studentList);

                // Step B: Get existing attendance for this date
                try {
                    const attRes = await axios.get(`http://localhost:9090/api/attendance/class/${selectedClass}/date/${attendanceDate}`);
                    const existingRecords = attRes.data || [];
                    
                    const statusMap = {};
                    studentList.forEach(s => {
                        const record = existingRecords.find(r => r.student?.studentId === s.studentId);
                        // Convert "present" -> "Present"
                        statusMap[s.studentId] = record 
                            ? record.status.charAt(0).toUpperCase() + record.status.slice(1) 
                            : "Present";
                    });
                    setAttendanceData(statusMap);
                } catch (attErr) {
                    // If 404 or error, default everyone to Present
                    const defaultStatus = {};
                    studentList.forEach(s => { defaultStatus[s.studentId] = "Present"; });
                    setAttendanceData(defaultStatus);
                }

            } catch (err) {
                console.error("Fetch Error:", err.response || err);
                setStudents([]);
            } finally {
                setLoading(false);
            }
        };

        fetchAttendance();
    }, [selectedClass, attendanceDate]);

    // Summary Calculations
    const presentCount = Object.values(attendanceData).filter(v => v === "Present").length;
    const absentCount = students.length - presentCount;

    const handleToggle = (studentId) => {
        setAttendanceData(prev => ({
            ...prev,
            [studentId]: prev[studentId] === "Present" ? "Absent" : "Present"
        }));
    };

    const handleSave = () => {
        const payload = students.map(s => ({
            student: { studentId: s.studentId },
            schoolClass: { classId: parseInt(selectedClass) }, 
            attendanceDate: attendanceDate,
            status: (attendanceData[s.studentId] || "Present").toLowerCase(),
            remarks: ""
        }));

        axios.post('http://localhost:9090/api/attendance/bulk', payload)
            .then(() => alert("Attendance Saved Successfully!"))
            .catch(err => alert("Error saving: " + err.message));
    };

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" sx={{ mb: 3, fontWeight: 'bold', color: '#1e3a8a' }}>
                Daily Attendance
            </Typography>

            {/* Stats Cards */}
            <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} sm={4}>
                    <Card sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2, bgcolor: '#f0f4ff' }}>
                        <Group sx={{ fontSize: 40, color: '#1e3a8a' }} />
                        <Box>
                            <Typography variant="body2">Total Students</Typography>
                            <Typography variant="h5" sx={{ fontWeight: 'bold' }}>{students.length}</Typography>
                        </Box>
                    </Card>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <Card sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2, bgcolor: '#f0fff4' }}>
                        <CheckCircle sx={{ fontSize: 40, color: '#2e7d32' }} />
                        <Box>
                            <Typography variant="body2">Present</Typography>
                            <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#2e7d32' }}>{presentCount}</Typography>
                        </Box>
                    </Card>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <Card sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2, bgcolor: '#fff5f5' }}>
                        <Cancel sx={{ fontSize: 40, color: '#d32f2f' }} />
                        <Box>
                            <Typography variant="body2">Absent</Typography>
                            <Typography variant="h5" sx={{ fontWeight: 'bold', color: '#d32f2f' }}>{absentCount}</Typography>
                        </Box>
                    </Card>
                </Grid>
            </Grid>

            {/* Selectors */}
            <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
                <TextField 
                    select label="Select Class" size="small" sx={{ width: 200 }}
                    value={selectedClass} onChange={(e) => setSelectedClass(e.target.value)}
                >
                    {classList.map(c => (
                        <MenuItem key={c.classId} value={c.classId}>
                            {c.className} (ID: {c.classId})
                        </MenuItem>
                    ))}
                </TextField>

                <TextField 
                    type="date" label="Date" size="small" 
                    InputLabelProps={{ shrink: true }}
                    value={attendanceDate} onChange={(e) => setAttendanceDate(e.target.value)}
                />
            </Box>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead sx={{ bgcolor: '#1e3a8a' }}>
                        <TableRow>
                            <TableCell sx={{ color: 'white' }}>Code</TableCell>
                            <TableCell sx={{ color: 'white' }}>Name</TableCell>
                            <TableCell align="center" sx={{ color: 'white' }}>Status</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {loading ? (
                            <TableRow><TableCell colSpan={3} align="center">Loading students...</TableCell></TableRow>
                        ) : students.length > 0 ? (
                            students.map((s) => (
                                <TableRow key={s.studentId}>
                                    <TableCell>{s.studentCode}</TableCell>
                                    <TableCell>{`${s.firstName} ${s.lastName}`}</TableCell>
                                    <TableCell align="center">
                                        <FormControlLabel
                                            control={
                                                <Switch 
                                                    checked={attendanceData[s.studentId] === "Present"}
                                                    onChange={() => handleToggle(s.studentId)}
                                                    color="success"
                                                />
                                            }
                                            label={attendanceData[s.studentId]}
                                        />
                                    </TableCell>
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={3} align="center" sx={{ py: 3 }}>
                                    {selectedClass ? `No students found in DB with Class ID ${selectedClass}` : "Please select a class."}
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                <Button variant="contained" onClick={handleSave} disabled={students.length === 0}>
                    Save Attendance
                </Button>
            </Box>
        </Box>
    );
};

export default AttendanceMarker;