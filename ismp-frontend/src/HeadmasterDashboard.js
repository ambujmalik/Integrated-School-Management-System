import React from 'react';
import { Box, Grid, Typography, Paper, Card, CardContent, LinearProgress, Button, Chip, Divider } from '@mui/material';
import { 
  AdminPanelSettings, Engineering, AccountBalance, 
  FactCheck, Business, Groups, Assessment, AssignmentLate 
} from '@mui/icons-material';

const HeadmasterDashboard = () => {
  return (
    <Box sx={{ p: 3, bgcolor: '#f1f5f9', minHeight: '100vh' }}>
      <Typography variant="h4" fontWeight="bold" sx={{ mb: 3, color: '#1e3a8a' }}>
        Headmaster's Executive Console
      </Typography>

      <Grid container spacing={3}>
        {/* Top Level Metrics */}
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #3b82f6' }}>
            <Groups color="primary" sx={{ fontSize: 40 }} />
            <Typography variant="h5" fontWeight="bold">32 / 1,240</Typography>
            <Typography variant="caption" color="text.secondary">STAFF / STUDENTS</Typography>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2, borderTop: '4px solid #10b981' }}>
            <Box display="flex" justifyContent="space-between" mb={1}>
              <Typography variant="subtitle2" fontWeight="bold">BUDGET UTILIZED (FY 2026)</Typography>
              <Typography variant="subtitle2">₹12,45,000 / ₹15,00,000</Typography>
            </Box>
            <LinearProgress variant="determinate" value={83} sx={{ height: 10, borderRadius: 5, bgcolor: '#e2e8f0' }} />
            <Typography variant="caption" color="text.secondary">83% of Annual Grant Spent</Typography>
          </Paper>
        </Grid>

        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #ef4444' }}>
            <Engineering color="error" sx={{ fontSize: 40 }} />
            <Typography variant="h5" fontWeight="bold">04</Typography>
            <Typography variant="caption" color="text.secondary">ACTIVE MAINTENANCE ISSUES</Typography>
          </Paper>
        </Grid>

        {/* Maintenance Priority Table */}
        <Grid item xs={12} md={7}>
          <Paper sx={{ p: 3, borderRadius: 2 }}>
            <Typography variant="h6" fontWeight="bold" gutterBottom>Infrastructure Audit & Maintenance</Typography>
            <Divider sx={{ mb: 2 }} />
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="body2">Roof Leakage - Science Lab</Typography>
              <Chip label="High Priority" color="error" size="small" />
            </Box>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
              <Typography variant="body2">Classroom 4B Wiring</Typography>
              <Chip label="Mid Priority" color="warning" size="small" />
            </Box>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="body2">Playground Leveling</Typography>
              <Chip label="Low Priority" color="info" size="small" />
            </Box>
          </Paper>
        </Grid>

        {/* Action Center */}
        <Grid item xs={12} md={5}>
          <Paper sx={{ p: 3, borderRadius: 2 }}>
            <Typography variant="h6" fontWeight="bold" gutterBottom>Pending Approvals</Typography>
            <Button fullWidth variant="contained" startIcon={<FactCheck />} sx={{ mb: 1.5, bgcolor: '#1e3a8a' }}>
              Approve Staff Leaves (3)
            </Button>
            <Button fullWidth variant="outlined" startIcon={<AccountBalance />} sx={{ mb: 1.5 }}>
              Budget Review Meeting
            </Button>
            <Button fullWidth variant="outlined" startIcon={<Business />}>
              Infrastructure Audit Report
            </Button>
          </Paper>
        </Grid>

        {/* Strategic Grid */}
        <Grid item xs={12}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={4}>
              <Card sx={{ bgcolor: '#eff6ff' }}>
                <CardContent>
                  <Assessment color="primary" />
                  <Typography variant="subtitle1" fontWeight="bold">School Report Card</Typography>
                  <Typography variant="caption">View academic performance trends</Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} sm={4}>
              <Card sx={{ bgcolor: '#f0fdf4' }}>
                <CardContent>
                  <AdminPanelSettings color="success" />
                  <Typography variant="subtitle1" fontWeight="bold">Accreditation</Typography>
                  <Typography variant="caption">Manage School Board Certification</Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} sm={4}>
              <Card sx={{ bgcolor: '#fef2f2' }}>
                <CardContent>
                  <AssignmentLate color="error" />
                  <Typography variant="subtitle1" fontWeight="bold">Staff Management</Typography>
                  <Typography variant="caption">Appraisals and Biometric Logs</Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
};

export default HeadmasterDashboard;