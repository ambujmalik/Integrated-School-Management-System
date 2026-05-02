package com.ismp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.Attendance;
import com.ismp.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {
	
	@Autowired
    private AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
        return new ResponseEntity<>(attendanceService.markAttendance(attendance), HttpStatus.CREATED);
    }

    @GetMapping("/class/{classId}/date/{date}")
    public ResponseEntity<List<Attendance>> getByClassAndDate(
            @PathVariable Integer classId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByClassAndDate(classId, date));
    }
    
    @PostMapping("/bulk")
    public ResponseEntity<String> saveBulkAttendance(@RequestBody List<Attendance> attendanceList) {
        // Use the service here!
        attendanceService.saveAllAttendance(attendanceList); 
        return ResponseEntity.ok("Attendance marked successfully");
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<Attendance> records = attendanceService.getAttendanceByDate(localDate);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
