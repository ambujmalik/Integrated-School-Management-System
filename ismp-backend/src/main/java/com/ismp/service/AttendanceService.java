package com.ismp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.Attendance;
import com.ismp.repository.AttendanceRepository;

import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

	@Autowired
    private AttendanceRepository attendanceRepository;

	public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date);
    }
	
    public Attendance markAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByClassAndDate(Integer classId, LocalDate date) {
        return attendanceRepository.findBySchoolClass_ClassIdAndAttendanceDate(classId, date);
    }
    
 // Inside AttendanceService.java
    public void saveAllAttendance(List<Attendance> attendanceList) {
        attendanceRepository.saveAll(attendanceList);
    }
    
    @Transactional
    public void saveAllAttendance(Integer classId, LocalDate date, List<Attendance> attendanceList) {
        // 1. Clear existing attendance for this class/date so we don't get duplicates
        attendanceRepository.deleteBySchoolClass_ClassIdAndAttendanceDate(classId, date);
        
        // 2. Save the new list
        attendanceRepository.saveAll(attendanceList);
    }
}
