import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Box, Grid, Card, CardContent, Typography, Avatar } from '@mui/material';
import { People, School, Business, Assignment } from '@mui/icons-material';

const StatsCard = ({ title, count, icon, color }) => (
  <Card sx={{ display: 'flex', alignItems: 'center', p: 2, boxShadow: 3 }}>
    <Avatar sx={{ bgcolor: color, width: 56, height: 56, mr: 2 }}>
      {icon}
    </Avatar>
    <Box>
      <Typography variant="subtitle2" color="text.secondary">
        {title}
      </Typography>
      <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
        {count}
      </Typography>
    </Box>
  </Card>
);

const Dashboard = () => {
  const [officerCount, setOfficerCount] = useState(0);
  const [studentCount, setStudentCount] = useState(0);

  useEffect(() => {
    // 1. Fetch Regional Officers Count
    axios.get('http://localhost:9090/api/regional-officers')
      .then(res => setOfficerCount(res.data.length))
      .catch(err => console.error("Error fetching officers:", err));

    // 2. Fetch Students Count
    axios.get('http://localhost:9090/api/students')
      .then(res => setStudentCount(res.data.length))
      .catch(err => console.error("Error fetching students:", err));
  }, []);

  return (
    <Box>
      <Typography variant="h4" gutterBottom sx={{ mb: 4, fontWeight: 'bold', color: '#1e3a8a' }}>
        Odisha Education State Overview
      </Typography>
      
      <Grid container spacing={3}>
        {/* Real Data from Backend */}
        <Grid item xs={12} sm={6} md={3}>
          <StatsCard 
            title="Regional Officers" 
            count={officerCount} 
            icon={<People />} 
            color="#1e3a8a" 
          />
        </Grid>

        {/* Placeholders for future modules */}
        <Grid item xs={12} sm={6} md={3}>
          <StatsCard 
            title="Total Schools" 
            count="4,230" 
            icon={<Business />} 
            color="#10b981" 
          />
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <StatsCard 
            title="Enrolled Students" 
           count={studentCount}
            icon={<School />} 
            color="#f59e0b" 
          />
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <StatsCard 
            title="Pending Exams" 
            count="12" 
            icon={<Assignment />} 
            color="#ef4444" 
          />
        </Grid>
      </Grid>

      <Box sx={{ mt: 6, p: 4, textAlign: 'center', backgroundColor: '#fff', borderRadius: 2, boxShadow: 1 }}>
         <Typography variant="h6" color="text.secondary">
            Welcome to the Integrated School Management Portal. 
            Use the sidebar to manage districts, schools, and academic records.
         </Typography>
      </Box>
    </Box>
  );
};

export default Dashboard;