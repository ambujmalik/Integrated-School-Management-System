import React from 'react';
import { Box, Grid, Typography, Paper, Button, Card, CardContent, Divider, LinearProgress, List, ListItem, ListItemText, Chip } from '@mui/material';
import { Domain, Assessment, LocationOn, AccountBalanceWallet, EventNote, Campaign, Group } from '@mui/icons-material';

const BeoDashboard = () => {
  return (
    <Box sx={{ p: 3, bgcolor: '#f0f2f5' }}>
      <Typography variant="h4" fontWeight="bold" sx={{ mb: 3, color: '#1a237e' }}>
        Block Education Console (BEO)
      </Typography>

      <Grid container spacing={3}>
        {/* Top Summary Cards */}
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Domain color="primary" fontSize="large" />
            <Box>
              <Typography variant="h5">42</Typography>
              <Typography variant="caption">TOTAL SCHOOLS</Typography>
            </Box>
          </Paper>
        </Grid>
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Group color="secondary" fontSize="large" />
            <Box>
              <Typography variant="h5">12,450</Typography>
              <Typography variant="caption">TOTAL STUDENTS</Typography>
            </Box>
          </Paper>
        </Grid>
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <Assessment color="success" fontSize="large" />
            <Box>
              <Typography variant="h5">94%</Typography>
              <Typography variant="caption">COMPLIANCE RATE</Typography>
            </Box>
          </Paper>
        </Grid>
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <EventNote color="error" fontSize="large" />
            <Box>
              <Typography variant="h5">08</Typography>
              <Typography variant="caption">PENDING INSPECTIONS</Typography>
            </Box>
          </Paper>
        </Grid>

        {/* Action Center & Compliance */}
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3, borderRadius: 2 }}>
            <Typography variant="h6" fontWeight="bold" gutterBottom>Fund Disbursement Status</Typography>
            <Box sx={{ mb: 2 }}>
              <Typography variant="body2">MDM (Mid-Day Meal) Grants</Typography>
              <LinearProgress variant="determinate" value={85} color="success" sx={{ height: 10, borderRadius: 5 }} />
            </Box>
            <Box sx={{ mb: 2 }}>
              <Typography variant="body2">Infrastructure Development Fund</Typography>
              <LinearProgress variant="determinate" value={40} color="warning" sx={{ height: 10, borderRadius: 5 }} />
            </Box>
            <Divider sx={{ my: 2 }} />
            <Box display="flex" gap={2}>
              <Button variant="contained" startIcon={<Campaign />}>Issue Block Circular</Button>
              <Button variant="outlined" startIcon={<LocationOn />}>Geo-Tag Schools</Button>
            </Box>
          </Paper>
        </Grid>

        {/* Inspection Schedule */}
        <Grid item xs={12} md={4}>
          <Card sx={{ height: '100%' }}>
            <CardContent>
              <Typography variant="h6" fontWeight="bold">Upcoming Inspections</Typography>
              <List dense>
                <ListItem divider>
                  <ListItemText primary="Government High School, Unit-6" secondary="Scheduled: April 10, 2026" />
                  <Chip label="Audit" size="small" color="primary" />
                </ListItem>
                <ListItem divider>
                  <ListItemText primary="Saraswati Shishu Vidya Mandir" secondary="Scheduled: April 12, 2026" />
                  <Chip label="Safety" size="small" color="warning" />
                </ListItem>
              </List>
              <Button fullWidth variant="contained" sx={{ mt: 2 }}>Schedule New Inspection</Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Data Aggregation Section */}
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" fontWeight="bold" gutterBottom>Data Aggregation & Reports</Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={4}>
                <Button fullWidth variant="outlined" startIcon={<Assessment />}>Generate Block Report</Button>
              </Grid>
              <Grid item xs={12} sm={4}>
                <Button fullWidth variant="outlined" startIcon={<AccountBalanceWallet />}>Financial Audit Management</Button>
              </Grid>
              <Grid item xs={12} sm={4}>
                <Button fullWidth variant="outlined">Sync EMIS Data</Button>
              </Grid>
            </Grid>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default BeoDashboard;