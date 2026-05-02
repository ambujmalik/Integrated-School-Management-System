package com.ismp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

	// Finds all attendance for a class on a specific date
    // Note: 'schoolClass' matches the variable name in our model
    List<Attendance> findBySchoolClass_ClassIdAndAttendanceDate(Integer classId, LocalDate date);
    
    // Finds all attendance for a specific student
    List<Attendance> findByStudent_StudentId(Integer studentId);
    
 // Spring parses this name: 
    // deleteBy -> Delete query
    // SchoolClass_ClassId -> Look inside SchoolClass for classId
    // And -> Second condition
    // AttendanceDate -> The date field
    void deleteBySchoolClass_ClassIdAndAttendanceDate(Integer classId, LocalDate date);
    
    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
}
