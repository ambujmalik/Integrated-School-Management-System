import React from 'react';
import { 
  Box, Grid, Typography, Paper, Avatar, 
  LinearProgress, ButtonBase, Card, CardContent 
} from '@mui/material';
import { 
  School, Event, Assignment, EmojiEvents, MenuBook, 
  Quiz, LibraryBooks, QuestionAnswer, ShowChart, Stars
} from '@mui/icons-material';

// Specialized Button for Dashboard Actions
const DashboardButton = ({ icon, title, color, subText }) => (
  <ButtonBase sx={{ width: '100%', textAlign: 'left' }}>
    <Card sx={{ 
      width: '100%', height: 120, display: 'flex', 
      flexDirection: 'column', justifyContent: 'center',
      alignItems: 'center', p: 1, borderRadius: 3,
      boxShadow: '0 4px 12px rgba(0,0,0,0.05)',
      transition: '0.3s', '&:hover': { transform: 'translateY(-5px)', boxShadow: 3 }
    }}>
      <Avatar sx={{ bgcolor: color, mb: 1, width: 45, height: 45 }}>{icon}</Avatar>
      <Typography variant="subtitle2" fontWeight="bold">{title}</Typography>
      <Typography variant="caption" color="text.secondary">{subText}</Typography>
    </Card>
  </ButtonBase>
);

const StudentDashboard = ({ studentData }) => {
  // studentData would be the 'Student' Entity from Java
  const stats = {
    grade: studentData?.currentClass || "10th",
    attendance: 94, // Calculated from Attendance Entity
    rank: "4th",
    points: 1250
  };

  return (
    <Box sx={{ p: 3, backgroundColor: '#f8fafc', minHeight: '100vh' }}>
      
      {/* 1. Profile Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" fontWeight="bold" color="#1e3a8a">
            ନମସ୍କାର, {studentData?.firstName || 'Student'}!
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            {studentData?.studentCode} | {studentData?.school?.schoolName || 'Odisha Govt High School'}
          </Typography>
        </Box>
        <Avatar sx={{ width: 60, height: 60, bgcolor: '#1e3a8a' }}>
           {studentData?.firstName?.charAt(0)}
        </Avatar>
      </Box>

      {/* 2. Top Stats Bar */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #3b82f6' }}>
            <Typography variant="caption" color="text.secondary">CURRENT GRADE</Typography>
            <Typography variant="h5" fontWeight="bold">{stats.grade}</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #10b981' }}>
            <Typography variant="caption" color="text.secondary">ATTENDANCE</Typography>
            <Typography variant="h5" fontWeight="bold" color="#10b981">{stats.attendance}%</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #f59e0b' }}>
            <Typography variant="caption" color="text.secondary">CLASS RANK</Typography>
            <Typography variant="h5" fontWeight="bold">{stats.rank}</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center', borderTop: '4px solid #7c3aed' }}>
            <Typography variant="caption" color="text.secondary">PENDING ASSIGNMENTS</Typography>
            <Typography variant="h5" fontWeight="bold" color="#ef4444">03</Typography>
          </Paper>
        </Grid>
      </Grid>

      {/* 3. Action Grid (Your Specific Requirements) */}
      <Typography variant="h6" sx={{ mb: 2, fontWeight: 'bold' }}>Learning Hub</Typography>
      <Grid container spacing={2}>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<Assignment />} title="View Assignments" subText="3 Active Tasks" color="#3b82f6" />
        </Grid>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<ShowChart />} title="Check Result" subText="Term 1 Available" color="#10b981" />
        </Grid>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<Event />} title="Time Table" subText="Today: 6 Periods" color="#6366f1" />
        </Grid>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<QuestionAnswer />} title="Ask Teacher" subText="2 Active Doubts" color="#f59e0b" />
        </Grid>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<MenuBook />} title="Study Material" subText="PDFs & Notes" color="#ec4899" />
        </Grid>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<Quiz />} title="Mock Test" subText="Science Quiz Ready" color="#8b5cf6" />
        </Grid>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<LibraryBooks />} title="Digital Library" subText="1000+ E-books" color="#14b8a6" />
        </Grid>
        <Grid item xs={6} sm={4} md={3}>
          <DashboardButton icon={<Stars />} title="Achievements" subText="Track Badges" color="#facc15" />
        </Grid>
      </Grid>

      {/* 4. Achievement Tracker */}
      <Paper sx={{ mt: 4, p: 3, borderRadius: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <EmojiEvents sx={{ mr: 1, color: '#facc15' }} />
          <Typography variant="h6">Achievement Tracker</Typography>
        </Box>
        <Typography variant="body2" color="text.secondary" gutterBottom>
          Progress to next level: <b>Scholar Level 2</b>
        </Typography>
        <LinearProgress variant="determinate" value={75} sx={{ height: 12, borderRadius: 5, my: 1, bgcolor: '#e2e8f0' }} />
        <Typography variant="caption">1250 / 2000 Experience Points (XP)</Typography>
      </Paper>
    </Box>
  );
};

export default StudentDashboard;