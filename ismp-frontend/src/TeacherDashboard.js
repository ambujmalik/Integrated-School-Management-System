import React from 'react';
import { Box, Grid, Typography, Paper, Avatar, ButtonBase, LinearProgress, Card } from '@mui/material';
import { 
  CheckCircle, Grade, MenuBook, Campaign, 
  Group, CloudUpload, Forum, AssignmentTurnedIn, CastForEducation 
} from '@mui/icons-material';

const TeacherDashboard = ({ teacherData }) => {
  
  const ActionCard = ({ icon, title, color, sub }) => (
    <ButtonBase sx={{ width: '100%' }}>
      <Paper sx={{ 
        p: 2, width: '100%', textAlign: 'center', borderRadius: 3,
        transition: '0.3s', '&:hover': { transform: 'translateY(-5px)', boxShadow: 4 } 
      }}>
        <Avatar sx={{ bgcolor: color, mx: 'auto', mb: 1 }}>{icon}</Avatar>
        <Typography variant="subtitle2" fontWeight="bold">{title}</Typography>
        <Typography variant="caption" color="text.secondary">{sub}</Typography>
      </Paper>
    </ButtonBase>
  );

  return (
    <Box sx={{ p: 3, bgcolor: '#f0f4f8', minHeight: '100vh' }}>
      {/* Welcome Header */}
      <Box sx={{ mb: 4, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <Box>
          <Typography variant="h4" fontWeight="bold" color="#1e3a8a">
            Welcome, {teacherData?.firstName || 'Teacher'}!
          </Typography>
          <Typography variant="body1" color="text.secondary">
            {teacherData?.designation || 'Class Teacher'} | Class 10-B
          </Typography>
        </Box>
        <Avatar sx={{ width: 56, height: 56, bgcolor: '#1e3a8a' }}>T</Avatar>
      </Box>

      {/* Main Stats Row */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 2, textAlign: 'center', bgcolor: '#fff', borderTop: '4px solid #10b981' }}>
            <Typography variant="caption" color="text.secondary">TOTAL STUDENTS</Typography>
            <Typography variant="h4" fontWeight="bold">42</Typography>
            <Typography variant="body2" color="success.main">Present Today: 38</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 2, textAlign: 'center', bgcolor: '#fff', borderTop: '4px solid #3b82f6' }}>
            <Typography variant="caption" color="text.secondary">NEXT LESSON</Typography>
            <Typography variant="h4" fontWeight="bold">Trigonometry</Typography>
            <Typography variant="body2" color="text.secondary">11:30 AM | Room 104</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 2, bgcolor: '#fff', borderTop: '4px solid #f59e0b' }}>
            <Typography variant="caption" color="text.secondary">SYLLABUS TRACKER</Typography>
            <Typography variant="h6" sx={{ mt: 1 }}>Mathematics: 65%</Typography>
            <LinearProgress variant="determinate" value={65} sx={{ height: 8, borderRadius: 5, mt: 1 }} />
          </Paper>
        </Grid>
      </Grid>

      {/* Feature Grid: Your Requested Tools */}
      <Typography variant="h6" fontWeight="bold" sx={{ mb: 2 }}>Management Console</Typography>
      <Grid container spacing={2}>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<CheckCircle />} title="Mark Attendance" sub="Daily Record" color="#10b981" />
        </Grid>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<Grade />} title="Grading & Tests" sub="Mark Entry" color="#ef4444" />
        </Grid>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<Group />} title="Student Roster" sub="Edit Profiles" color="#6366f1" />
        </Grid>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<Campaign />} title="Send Circular" sub="Alert Parents" color="#f59e0b" />
        </Grid>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<CloudUpload />} title="e-Content Dev" sub="Upload Videos" color="#ec4899" />
        </Grid>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<AssignmentTurnedIn />} title="Submit Grades" sub="Final Results" color="#14b8a6" />
        </Grid>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<MenuBook />} title="Syllabus Tracker" sub="Unit Progress" color="#8b5cf6" />
        </Grid>
        <Grid item xs={6} sm={4} md={2.4}>
          <ActionCard icon={<Forum />} title="Parent Comm." sub="Chat/Messages" color="#0ea5e9" />
        </Grid>
      </Grid>
    </Box>
  );
};

export default TeacherDashboard;