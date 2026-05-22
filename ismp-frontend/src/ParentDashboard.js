import React from 'react';
import { Box, Grid, Typography, Paper, Button, Card, CardContent, Divider, Chip, LinearProgress } from '@mui/material';
import { Payment, Event, Chat, Security, Download, Info } from '@mui/icons-material';

const ParentDashboard = () => {
  return (
    <Box sx={{ p: 3, bgcolor: '#f8fafc' }}>
      {/* Header Section */}
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" fontWeight="bold" color="#0f172a">Parent Portal</Typography>
          <Typography variant="body1" color="text.secondary">Monitoring: <b>Ravi Kumar</b> (Class 10-B)</Typography>
        </Box>
        <Chip label="Fees Paid: 80%" color="success" variant="outlined" />
      </Box>

      <Grid container spacing={3}>
        {/* Child Performance Card */}
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3, borderRadius: 3 }}>
            <Typography variant="h6" gutterBottom fontWeight="bold">Child Performance Summary</Typography>
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={4}>
                <Typography variant="caption" color="text.secondary">AVERAGE GRADE</Typography>
                <Typography variant="h4" color="primary" fontWeight="bold">A- (84%)</Typography>
              </Grid>
              <Grid item xs={4}>
                <Typography variant="caption" color="text.secondary">ATTENDANCE</Typography>
                <Typography variant="h4" color="success.main" fontWeight="bold">92%</Typography>
              </Grid>
              <Grid item xs={4}>
                <Button variant="outlined" startIcon={<Download />} fullWidth sx={{ mt: 1 }}>Report Card</Button>
              </Grid>
            </Grid>
            <Divider sx={{ my: 2 }} />
            <Typography variant="body2" sx={{ mb: 1 }}>Mathematics Progress</Typography>
            <LinearProgress variant="determinate" value={75} sx={{ height: 10, borderRadius: 5 }} />
          </Paper>
        </Grid>

        {/* Next PTM & Fee Alerts */}
        <Grid item xs={12} md={4}>
          <Card sx={{ bgcolor: '#fff4e5', mb: 2, borderRadius: 3 }}>
            <CardContent>
              <Box display="flex" alignItems="center" gap={1}>
                <Event color="warning" />
                <Typography variant="subtitle1" fontWeight="bold">Next PTM</Typography>
              </Box>
              <Typography variant="h6" sx={{ mt: 1 }}>April 20, 2026</Typography>
              <Typography variant="caption">10:00 AM - School Library</Typography>
            </CardContent>
          </Card>

          <Card sx={{ bgcolor: '#fee2e2', borderRadius: 3 }}>
            <CardContent>
              <Box display="flex" alignItems="center" gap={1}>
                <Payment color="error" />
                <Typography variant="subtitle1" fontWeight="bold">Pending Fees</Typography>
              </Box>
              <Typography variant="h6" sx={{ mt: 1 }}>₹2,450.00</Typography>
              <Button size="small" variant="contained" color="error" fullWidth sx={{ mt: 1 }}>Pay Now</Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Safety and Notices */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3, borderRadius: 3 }}>
            <Box display="flex" alignItems="center" gap={1} mb={2}>
              <Security color="primary" />
              <Typography variant="h6" fontWeight="bold">Safety Monitoring</Typography>
            </Box>
            <Typography variant="body2" color="text.secondary">✓ Child checked into school bus: <b>08:15 AM</b></Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>✓ Attendance marked in Class: <b>09:05 AM</b></Typography>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3, borderRadius: 3 }}>
            <Box display="flex" alignItems="center" gap={1} mb={2}>
              <Chat color="secondary" />
              <Typography variant="h6" fontWeight="bold">Contact & Support</Typography>
            </Box>
            <Box display="flex" gap={2}>
              <Button fullWidth variant="outlined">Chat with Teacher</Button>
              <Button fullWidth variant="outlined">School Calendar</Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default ParentDashboard;