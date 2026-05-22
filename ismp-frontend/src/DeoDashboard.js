import React from 'react';
import { Grid, Paper, Typography, Box, Button, Card, CardContent } from '@mui/material';
import { AssignmentTurnedIn, Map, TrendingUp, LocalAtm, ErrorOutline } from '@mui/icons-material';

const DeoDashboard = () => {
  const stats = [
    { label: 'Total Blocks', value: '12', icon: <Map color="primary" /> },
    { label: 'Avg. Literacy Rate', value: '78.4%', icon: <TrendingUp color="success" /> },
    { label: 'Pending Schemes', value: '24', icon: <ErrorOutline color="error" /> },
    { label: 'Teacher Vacancies', value: '142', icon: <AssignmentTurnedIn color="warning" /> },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>District Education Overview</Typography>
      
      <Grid container spacing={3} mb={4}>
        {stats.map((stat, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Paper elevation={2} sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
              {stat.icon}
              <Box>
                <Typography variant="caption" color="textSecondary">{stat.label}</Typography>
                <Typography variant="h6">{stat.value}</Typography>
              </Box>
            </Paper>
          </Grid>
        ))}
      </Grid>

      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Priority Actions</Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Button variant="outlined" fullWidth>Approve Teacher Transfers (14 Pending)</Button>
                <Button variant="outlined" fullWidth>Review District Budget Q3</Button>
                <Button variant="outlined" color="secondary" fullWidth>Issue State Annual Report</Button>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={4}>
          <Card sx={{ bgcolor: '#1e293b', color: 'white' }}>
            <CardContent>
              <Typography variant="h6">Scheme Monitoring</Typography>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>MDM Mid-Day Meal: 94%</Typography>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>Mo School Abhiyan: 82%</Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default DeoDashboard;