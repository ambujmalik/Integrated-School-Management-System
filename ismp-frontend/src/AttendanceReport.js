import React, { useState } from 'react';
import axios from 'axios';
import { 
  Box, Typography, Table, TableBody, TableCell, TableContainer, 
  TableHead, TableRow, Paper, TextField, Button, Grid, Card 
} from '@mui/material';
import { Download } from '@mui/icons-material';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

const AttendanceReport = () => {
    const [reportDate, setReportDate] = useState(new Date().toISOString().split('T')[0]);
    const [records, setRecords] = useState([]);
    const [summary, setSummary] = useState({ present: 0, absent: 0 });

    const fetchReport = () => {
        axios.get(`http://localhost:9090/api/attendance/date/${reportDate}`)
            .then(res => {
                const data = Array.isArray(res.data) ? res.data : [];
                setRecords(data);
                const p = data.filter(r => r.status.toLowerCase() === 'present').length;
                const a = data.length - p;
                setSummary({ present: p, absent: a });
            })
            .catch(err => console.error("Report Fetch Error:", err));
    };

    const downloadPDF = () => {
        const doc = new jsPDF();
        const today = new Date().toLocaleString();

        // 1. Header Design
        doc.setFillColor(30, 58, 138); // Dark Blue
        doc.rect(0, 0, 210, 35, 'F');
        doc.setTextColor(255, 255, 255);
        doc.setFontSize(20);
        doc.text("ISMP ODISHA", 14, 18);
        doc.setFontSize(10);
        doc.text("Integrated School Management Portal - Attendance System", 14, 25);

        // 2. Report Summary Info
        doc.setTextColor(0, 0, 0);
        doc.setFontSize(11);
        doc.text(`Target Date: ${reportDate}`, 14, 45);
        doc.text(`Generated On: ${today}`, 14, 52);
        doc.text(`Summary: ${summary.present} Present | ${summary.absent} Absent`, 14, 59);

        // 3. Define Table Columns (4 columns to match data)
        const tableColumn = ["Roll No", "Student Name", "Class", "Status"];

        // 4. Map Data (Cleaned up: No more double-pushing)
        const tableRows = records.map(r => [
            r.student.studentCode || 'N/A',
            `${r.student.firstName} ${r.student.lastName}`,
            r.schoolClass?.className || "Class " + r.student.currentClass,
            r.status.toUpperCase()
        ]);

        // 5. Generate Table
        autoTable(doc, {
            head: [tableColumn],
            body: tableRows,
            startY: 65,
            theme: 'grid',
            headStyles: { fillColor: [30, 58, 138], halign: 'center' },
            columnStyles: {
                0: { halign: 'center' },
                3: { halign: 'center', fontStyle: 'bold' }
            }
        });

        doc.save(`Attendance_Report_${reportDate}.pdf`);
    };

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" sx={{ mb: 3, fontWeight: 'bold', color: '#1e3a8a' }}>
                Attendance Report
            </Typography>

            <Box sx={{ display: 'flex', gap: 2, mb: 4, alignItems: 'center' }}>
                <TextField 
                    type="date" label="Select Date" size="small"
                    InputLabelProps={{ shrink: true }}
                    value={reportDate} onChange={(e) => setReportDate(e.target.value)}
                />
                <Button variant="contained" onClick={fetchReport}>Generate Report</Button>
                
                <Button 
                    variant="contained" 
                    color="success" 
                    startIcon={<Download />} 
                    onClick={downloadPDF}
                    disabled={records.length === 0}
                >
                    Download PDF
                </Button>
            </Box>

            {records.length > 0 && (
                <>
                    <Grid container spacing={2} sx={{ mb: 4 }}>
                        <Grid item xs={6} sm={3}>
                            <Card sx={{ p: 2, bgcolor: '#e8f5e9', textAlign: 'center', borderLeft: '5px solid green' }}>
                                <Typography variant="h6">{summary.present}</Typography>
                                <Typography variant="body2">Total Present</Typography>
                            </Card>
                        </Grid>
                        <Grid item xs={6} sm={3}>
                            <Card sx={{ p: 2, bgcolor: '#ffebee', textAlign: 'center', borderLeft: '5px solid red' }}>
                                <Typography variant="h6">{summary.absent}</Typography>
                                <Typography variant="body2">Total Absent</Typography>
                            </Card>
                        </Grid>
                    </Grid>

                    <TableContainer component={Paper} sx={{ boxShadow: 3 }}>
                        <Table>
                            <TableHead sx={{ bgcolor: '#1e3a8a' }}>
                                <TableRow>
                                    <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Roll No</TableCell>
                                    <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Student Name</TableCell>
                                    <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Class</TableCell>
                                    <TableCell align="center" sx={{ color: 'white', fontWeight: 'bold' }}>Status</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {records.map((r, index) => (
                                    <TableRow key={index} hover>
                                        <TableCell>{r.student.studentCode || 'N/A'}</TableCell>
                                        <TableCell>{`${r.student.firstName} ${r.student.lastName}`}</TableCell>
                                        <TableCell>{r.schoolClass?.className || 'N/A'}</TableCell>
                                        <TableCell align="center" sx={{ 
                                            color: r.status.toLowerCase() === 'present' ? 'green' : 'red',
                                            fontWeight: 'bold'
                                        }}>
                                            {r.status.toUpperCase()}
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </>
            )}
        </Box>
    );
};

export default AttendanceReport;