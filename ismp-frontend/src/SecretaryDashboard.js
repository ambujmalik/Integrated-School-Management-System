import React from 'react';
import { Grid, Paper, Typography, Box, Button, LinearProgress } from '@mui/material';
import { Gavel, Warning, Language, CorporateFare, AccountBalance } from '@mui/icons-material';

const SecretaryDashboard = () => {
  return (
    <Box p={3}>
      <Typography variant="h4" fontWeight="bold" gutterBottom>Apex Policy Control</Typography>
      
      <Grid container spacing={3}>
        {/* State-Level KPIs */}
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #1e3a8a' }}>
            <Typography variant="subtitle2" color="textSecondary">Total Districts</Typography>
            <Typography variant="h3">30</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #10b981' }}>
            <Typography variant="subtitle2" color="textSecondary">Avg Pass Rate (State)</Typography>
            <Typography variant="h3">84.2%</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2, bgcolor: '#fee2e2' }}>
            <Typography variant="subtitle2" color="#b91c1c" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <Warning fontSize="small" /> Critical Issues (Disaster Plan)
            </Typography>
            <Typography variant="body2" mt={1}>Cyclone Alert: 4 Coastal Districts require emergency school closure directives.</Typography>
            <Button size="small" variant="contained" color="error" sx={{ mt: 1 }}>Allocate Emergency Funds</Button>
          </Paper>
        </Grid>

        {/* Strategic Actions */}
        <Grid item xs={12} md={8}>
          <Typography variant="h6" mt={2} mb={1}>Strategic Planning & Directives</Typography>
          <Paper sx={{ p: 0 }}>
            <Box p={2} borderBottom="1px solid #eee">
              <Typography variant="subtitle1">Policy Implementation: 5T Initiative</Typography>
              <LinearProgress variant="determinate" value={75} sx={{ my: 1, height: 8, borderRadius: 5 }} />
            </Box>
            <Box p={2} display="flex" justifyContent="space-between" alignItems="center">
              <Typography>Set State-Wide Policy Directives (2026-27)</Typography>
              <Button startIcon={<Gavel />} variant="outlined">Draft</Button>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12} md={4}>
          <Typography variant="h6" mt={2} mb={1}>Divisional Metrics</Typography>
          <Paper sx={{ p: 2 }}>
            <Button fullWidth variant="text" sx={{ justifyContent: 'start' }}>Central Division Analysis</Button>
            <Button fullWidth variant="text" sx={{ justifyContent: 'start' }}>Northern Division Analysis</Button>
            <Button fullWidth variant="text" sx={{ justifyContent: 'start' }}>Southern Division Analysis</Button>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default SecretaryDashboard;