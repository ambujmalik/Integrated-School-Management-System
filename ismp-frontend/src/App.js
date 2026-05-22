import React, { useState, useMemo } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import { 
  Box, Drawer, AppBar, Toolbar, List, Typography, 
  ListItem, ListItemButton, ListItemIcon, ListItemText, CssBaseline, Button 
} from '@mui/material';
import { 
  People, School, Receipt, Dashboard as DashboardIcon, 
  CheckCircle, Assessment, Logout, Assignment, MenuBook, Quiz, Stars,
  Grade, Campaign, CloudUpload, Forum, AssignmentTurnedIn,
  Payment, Security, NotificationsActive, Event, Business, 
  AccountBalance, FactCheck, Domain, LocationOn, AccountBalanceWallet, Group,
  TrendingUp, Policy, Map,
  Gavel, Warning, Language, CorporateFare // Secretary specific icons
} from '@mui/icons-material';
import { jwtDecode } from 'jwt-decode';

// Utilities
import { hasAccess } from './utils/permissions';

// Components
import OfficerList from './OfficerList';
import Dashboard from './Dashboard';
import StudentDashboard from './StudentDashboard'; 
import TeacherDashboard from './TeacherDashboard';
import ParentDashboard from './ParentDashboard'; 
import HeadmasterDashboard from './HeadmasterDashboard';
import BeoDashboard from './BeoDashboard';
import DeoDashboard from './DeoDashboard';
import SecretaryDashboard from './SecretaryDashboard'; // Ensure this file is created!
import StudentList from './StudentList';
import AttendanceMarker from './AttendanceMarker';
import Login from './Login';

const drawerWidth = 240;

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));

  const userRole = useMemo(() => {
    const token = localStorage.getItem("token");
    if (!token) return null;

    try {
      const decoded = jwtDecode(token);
      return decoded.role; 
    } catch (error) {
      console.error("Token invalid.", error);
      localStorage.removeItem("token");
      setIsLoggedIn(false);
      return null;
    }
  }, [isLoggedIn]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsLoggedIn(false);
  };

  const MenuItem = ({ to, icon, label, permission }) => {
    if (permission && !hasAccess(userRole, permission)) return null;
    return (
      <ListItem disablePadding component={Link} to={to}>
        <ListItemButton>
          <ListItemIcon>{icon}</ListItemIcon>
          <ListItemText primary={label} />
        </ListItemButton>
      </ListItem>
    );
  };

  if (!isLoggedIn) {
    return <Login onLoginSuccess={() => setIsLoggedIn(true)} />;
  }

  const role = userRole?.toLowerCase();

  const getAppBarTitle = () => {
    switch(role) {
      case 'teacher': return 'Teacher Console';
      case 'student': return 'Student Portal';
      case 'parent': return 'Parent Portal';
      case 'headmaster': return 'Headmaster Console';
      case 'beo': return 'Block Education Console';
      case 'deo': return 'District Education Console';
      case 'secretary': return 'Apex Policy Control'; // Secretary Title
      default: return 'Admin Portal';
    }
  };

  return (
    <Router>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        
        <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1, backgroundColor: '#1e3a8a' }}>
          <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Typography variant="h6" noWrap component="div">
              ISMP Odisha - {getAppBarTitle()}
            </Typography>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
              <Typography variant="caption" sx={{ bgcolor: 'rgba(255,255,255,0.1)', px: 1, borderRadius: 1 }}>
                Role: {userRole}
              </Typography>
              <Button color="inherit" startIcon={<Logout />} onClick={handleLogout} size="small">
                Logout
              </Button>
            </Box>
          </Toolbar>
        </AppBar>

        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth, flexShrink: 0,
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
          }}
        >
          <Toolbar />
          <Box sx={{ overflow: 'auto' }}>
            <List>
              <MenuItem to="/" icon={<DashboardIcon />} label="Dashboard" />

              {/* --- SECRETARY MENU --- */}
              {role === 'secretary' && (
                <>
                  <MenuItem to="/state-policy" icon={<Gavel color="primary" />} label="Policy Directives" />
                  <MenuItem to="/disaster-mgmt" icon={<Warning color="error" />} label="Disaster/Crisis" />
                  <MenuItem to="/divisional-metrics" icon={<Language color="info" />} label="Divisional Metrics" />
                  <MenuItem to="/emergency-funds" icon={<AccountBalance color="success" />} label="Emergency Funds" />
                  <MenuItem to="/strategic-plan" icon={<CorporateFare color="secondary" />} label="Strategic Planning" />
                </>
              )}

              {/* --- DEO MENU --- */}
              {role === 'deo' && (
                <>
                  <MenuItem to="/district-blocks" icon={<Map color="primary" />} label="Total Blocks" />
                  <MenuItem to="/teacher-transfers" icon={<AssignmentTurnedIn color="success" />} label="Approve Transfers" />
                  <MenuItem to="/district-policy" icon={<Policy color="info" />} label="Policy Updates" />
                  <MenuItem to="/district-budget" icon={<AccountBalanceWallet color="warning" />} label="Budget Review" />
                  <MenuItem to="/state-reports" icon={<Assessment color="secondary" />} label="Issue State Report" />
                  <MenuItem to="/scheme-monitoring" icon={<FactCheck color="error" />} label="Scheme Monitoring" />
                </>
              )}

              {/* --- BEO MENU --- */}
              {role === 'beo' && (
                <>
                  <MenuItem to="/block-schools" icon={<Domain color="primary" />} label="Total Schools" />
                  <MenuItem to="/inspections" icon={<FactCheck color="error" />} label="Pending Inspections" />
                  <MenuItem to="/fund-management" icon={<AccountBalanceWallet color="success" />} label="Fund Disbursement" />
                  <MenuItem to="/block-reports" icon={<Assessment color="info" />} label="Generate Reports" />
                  <MenuItem to="/geo-tagging" icon={<LocationOn color="secondary" />} label="Geo Tagging" />
                  <MenuItem to="/block-circulars" icon={<Campaign color="warning" />} label="Block Circulars" />
                </>
              )}

              {/* --- HEADMASTER MENU --- */}
              {role === 'headmaster' && (
                <>
                  <MenuItem to="/staff-management" icon={<People color="primary" />} label="Staff Management" />
                  <MenuItem to="/budget" icon={<AccountBalance color="success" />} label="Financial Mgmt" />
                  <MenuItem to="/infrastructure" icon={<Business color="secondary" />} label="Infrastructure" />
                  <MenuItem to="/approvals" icon={<FactCheck color="warning" />} label="Approve Leaves" />
                  <MenuItem to="/school-report" icon={<Assessment color="info" />} label="School Report Card" />
                </>
              )}

              {/* --- TEACHER MENU --- */}
              {role === 'teacher' && (
                <>
                  <MenuItem to="/attendance" icon={<CheckCircle color="success" />} label="Mark Attendance" />
                  <MenuItem to="/grading" icon={<Grade color="error" />} label="Grading" />
                  <MenuItem to="/syllabus" icon={<AssignmentTurnedIn color="primary" />} label="Syllabus Tracker" />
                  <MenuItem to="/e-content" icon={<CloudUpload color="secondary" />} label="e-Content Dev" />
                  <MenuItem to="/circulars" icon={<Campaign color="warning" />} label="Send Circular" />
                  <MenuItem to="/parent-comm" icon={<Forum color="info" />} label="Parent Chat" />
                </>
              )}

              {/* --- STUDENT MENU --- */}
              {role === 'student' && (
                <>
                  <MenuItem to="/assignments" icon={<Assignment color="primary" />} label="Assignments" />
                  <MenuItem to="/study-material" icon={<MenuBook color="secondary" />} label="Study Material" />
                  <MenuItem to="/mock-tests" icon={<Quiz color="success" />} label="Mock Tests" />
                  <MenuItem to="/achievements" icon={<Stars sx={{ color: '#facc15' }} />} label="Achievements" />
                </>
              )}

              {/* --- PARENT MENU --- */}
              {role === 'parent' && (
                <>
                  <MenuItem to="/child-performance" icon={<Assessment color="primary" />} label="Child Progress" />
                  <MenuItem to="/fees/pay" icon={<Payment color="success" />} label="Pay Fees" />
                  <MenuItem to="/school-calendar" icon={<Event color="info" />} label="School Calendar" />
                  <MenuItem to="/notices" icon={<NotificationsActive color="warning" />} label="School Notices" />
                  <MenuItem to="/safety" icon={<Security color="error" />} label="Safety Monitoring" />
                </>
              )}

              {/* --- ADMIN / STAFF MENU --- */}
              {(role !== 'student' && role !== 'teacher' && role !== 'parent' && role !== 'headmaster' && role !== 'beo' && role !== 'deo' && role !== 'secretary') && (
                <>
                  <MenuItem to="/attendance" icon={<CheckCircle color="success" />} label="Mark Attendance" permission="attendance" />
                  <MenuItem to="/students" icon={<School />} label="Students List" permission="students" />
                  <MenuItem to="/officers" icon={<People />} label="Regional Officers" permission="officers" />
                  <MenuItem to="/fees" icon={<Receipt />} label="Fees & Exams" permission="fees-exams" />
                </>
              )}
            </List>
          </Box>
        </Drawer>

        <Box component="main" sx={{ flexGrow: 1, p: 3, backgroundColor: '#f4f6f8', minHeight: '100vh' }}>
          <Toolbar />
          <Routes>
            <Route 
              path="/" 
              element={
                role === 'student' ? <StudentDashboard /> : 
                role === 'teacher' ? <TeacherDashboard /> : 
                role === 'parent' ? <ParentDashboard /> : 
                role === 'headmaster' ? <HeadmasterDashboard /> : 
                role === 'beo' ? <BeoDashboard /> : 
                role === 'deo' ? <DeoDashboard /> : 
                role === 'secretary' ? <SecretaryDashboard /> : 
                <Dashboard role={userRole} />
              } 
            />
            
            {/* Management Routes */}
            {hasAccess(userRole, 'officers') && <Route path="/officers" element={<OfficerList />} />}
            {hasAccess(userRole, 'students') && <Route path="/students" element={<StudentList />} />}
            <Route path="/attendance" element={<AttendanceMarker />} />
            
            {/* Secretary Specific Routes */}
            {role === 'secretary' && (
              <>
                <Route path="/state-policy" element={<Box p={3}>State-wide Policy Directives Management Coming Soon</Box>} />
                <Route path="/disaster-mgmt" element={<Box p={3}>Disaster and Crisis Resource Management Coming Soon</Box>} />
                <Route path="/divisional-metrics" element={<Box p={3}>Divisional Performance Analytics Coming Soon</Box>} />
                <Route path="/emergency-funds" element={<Box p={3}>Emergency Fund Allocation Panel Coming Soon</Box>} />
                <Route path="/strategic-plan" element={<Box p={3}>Strategic Education Planning 2026-2030 Coming Soon</Box>} />
              </>
            )}

            {/* DEO Specific Routes */}
            {role === 'deo' && (
              <>
                <Route path="/district-blocks" element={<Box p={3}>District-Block Coordination Analytics Coming Soon</Box>} />
                <Route path="/teacher-transfers" element={<Box p={3}>Teacher Transfer Approval Panel Coming Soon</Box>} />
                <Route path="/district-policy" element={<Box p={3}>District Policy Update System Coming Soon</Box>} />
                <Route path="/district-budget" element={<Box p={3}>District Budget Review Coming Soon</Box>} />
                <Route path="/state-reports" element={<Box p={3}>State Report Issuance Coming Soon</Box>} />
                <Route path="/scheme-monitoring" element={<Box p={3}>Scheme Monitoring Details Coming Soon</Box>} />
              </>
            )}

            {/* BEO Specific Routes */}
            {role === 'beo' && (
              <>
                <Route path="/block-reports" element={<Box p={3}>Detailed Block Report Generation Coming Soon</Box>} />
                <Route path="/inspections" element={<Box p={3}>Inspection Scheduling & Audit Logs Coming Soon</Box>} />
                <Route path="/geo-tagging" element={<Box p={3}>School Geo-Tagging & Map Integration Coming Soon</Box>} />
                <Route path="/block-schools" element={<Box p={3}>Block School Directory Coming Soon</Box>} />
                <Route path="/fund-management" element={<Box p={3}>Fund Disbursement Tracker Coming Soon</Box>} />
                <Route path="/block-circulars" element={<Box p={3}>Block Level Circulars Coming Soon</Box>} />
              </>
            )}

            {/* Headmaster Specific Routes */}
            {role === 'headmaster' && (
              <>
                <Route path="/budget" element={<Box p={3}>Financial Management Coming Soon</Box>} />
                <Route path="/infrastructure" element={<Box p={3}>Infrastructure Audit Coming Soon</Box>} />
                <Route path="/staff-management" element={<Box p={3}>Staff Management Coming Soon</Box>} />
                <Route path="/approvals" element={<Box p={3}>Leave Approval Dashboard Coming Soon</Box>} />
                <Route path="/school-report" element={<Box p={3}>School Performance Card Coming Soon</Box>} />
              </>
            )}

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Box>
      </Box>
    </Router>
  );
}

export default App;