export const ROLE_PERMISSIONS = {
  // ADD THIS LINE
  ADMIN: ['dashboard', 'officers', 'attendance-report', 'students', 'attendance', 'fees-exams'],
  
  SECRETARY: ['dashboard', 'officers', 'attendance-report', 'students', 'fees-exams'],
  REGIONAL_OFFICER: ['dashboard', 'attendance-report', 'students'],
  BEO: ['dashboard', 'attendance-report', 'students', 'attendance'],
  HEADMASTER: ['dashboard', 'attendance', 'attendance-report', 'students', 'fees-exams'],
  TEACHER: ['dashboard', 'attendance', 'students'],
  STUDENT: ['dashboard', 'assignments', 'study-material', 'mock-tests'],
  PARENT: ['dashboard', 'my-progress', 'fees-exams'],
};

export const hasAccess = (role, permission) => {
  if (!role) return false;
  
  const normalizedRole = role.toUpperCase(); 
  
  // Safety check: ensure the role actually exists in our map
  if (!ROLE_PERMISSIONS[normalizedRole]) {
      console.warn(`Role ${normalizedRole} not found in ROLE_PERMISSIONS`);
      return false;
  }

  return ROLE_PERMISSIONS[normalizedRole].includes(permission);
};