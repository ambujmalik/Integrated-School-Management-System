package com.ismp.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismp.model.Attendance;
import com.ismp.service.AttendanceService;

/**
 * Attendance Controller
 * 
 * Manages all attendance-related REST API endpoints.
 * Provides functionality to:
 * - Mark attendance for students
 * - Retrieve attendance records by class and date
 * - Retrieve attendance records by specific date
 * - Perform bulk attendance operations
 * 
 * CORS is managed centrally by SecurityConfig.
 * All endpoints require authentication as configured in SecurityConfig.
 * 
 * @author School Management System
 * @version 2.0
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

	private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

	@Autowired
	private AttendanceService attendanceService;

	/**
	 * Mark attendance for a student
	 * 
	 * @param attendance The attendance record containing student ID, date, and status
	 * @return ResponseEntity with created attendance record and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if data is invalid
	 * 
	 * @example
	 * POST /api/attendance
	 * Body: { "studentId": 1, "date": "2024-06-20", "status": "PRESENT" }
	 * Response: 201 Created with attendance object
	 */
	@PostMapping
	public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
		try {
			if (attendance == null) {
				logger.warn("Attempt to mark null attendance");
				return ResponseEntity.badRequest().build();
			}

			Attendance savedAttendance = attendanceService.markAttendance(attendance);
			logger.info("Attendance marked successfully for student ID: {} on date: {}", 
				attendance.getStudentId(), attendance.getDate());
			return new ResponseEntity<>(savedAttendance, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid attendance data provided: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error marking attendance", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Get attendance records by class and date
	 * 
	 * @param classId The ID of the class (must be positive)
	 * @param date The date for which to fetch attendance (ISO format: YYYY-MM-DD)
	 * @return ResponseEntity with list of attendance records and 200 (OK) status,
	 *         or 204 (NO_CONTENT) if no records exist,
	 *         or 400 (BAD_REQUEST) if parameters are invalid
	 * 
	 * @example
	 * GET /api/attendance/class/1/date/2024-06-20
	 * Response: 200 OK with list of attendance records
	 */
	@GetMapping("/class/{classId}/date/{date}")
	public ResponseEntity<List<Attendance>> getByClassAndDate(
			@PathVariable Integer classId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		try {
			if (classId == null || classId <= 0) {
				logger.warn("Invalid classId provided: {}", classId);
				return ResponseEntity.badRequest().build();
			}

			if (date == null) {
				logger.warn("Invalid date provided");
				return ResponseEntity.badRequest().build();
			}

			List<Attendance> records = attendanceService.getAttendanceByClassAndDate(classId, date);
			if (records.isEmpty()) {
				logger.info("No attendance records found for classId: {} on date: {}", classId, date);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			logger.info("Retrieved {} attendance records for classId: {} on date: {}", records.size(), classId, date);
			return ResponseEntity.ok(records);
		} catch (Exception e) {
			logger.error("Error retrieving attendance for class: {} on date: {}", classId, date, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Perform bulk attendance marking
	 * 
	 * Creates multiple attendance records in a single operation.
	 * 
	 * @param attendanceList List of attendance records to be saved
	 * @return ResponseEntity with success message and 201 (CREATED) status,
	 *         or 400 (BAD_REQUEST) if list is empty or invalid
	 * 
	 * @example
	 * POST /api/attendance/bulk
	 * Body: [{ "studentId": 1, "date": "2024-06-20", "status": "PRESENT" },
	 *        { "studentId": 2, "date": "2024-06-20", "status": "ABSENT" }]
	 * Response: 201 Created with "Bulk attendance marked successfully"
	 */
	@PostMapping("/bulk")
	public ResponseEntity<String> saveBulkAttendance(@RequestBody List<Attendance> attendanceList) {
		try {
			if (attendanceList == null || attendanceList.isEmpty()) {
				logger.warn("Attempt to save empty or null attendance list");
				return ResponseEntity.badRequest().body("Attendance list cannot be empty");
			}

			attendanceService.saveAllAttendance(attendanceList);
			logger.info("Bulk attendance saved successfully for {} records", attendanceList.size());
			return ResponseEntity.status(HttpStatus.CREATED).body("Bulk attendance marked successfully");
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid attendance data in bulk operation: {}", e.getMessage());
			return ResponseEntity.badRequest().body("Invalid attendance data: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error saving bulk attendance", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	/**
	 * Get attendance records by date
	 * 
	 * @param date The date for which to fetch attendance (ISO format: YYYY-MM-DD)
	 * @return ResponseEntity with list of attendance records and 200 (OK) status,
	 *         or 204 (NO_CONTENT) if no records exist,
	 *         or 400 (BAD_REQUEST) if date format is invalid
	 * 
	 * @example
	 * GET /api/attendance/date/2024-06-20
	 * Response: 200 OK with list of all attendance records on that date
	 */
	@GetMapping("/date/{date}")
	public ResponseEntity<List<Attendance>> getAttendanceByDate(@PathVariable String date) {
		try {
			if (date == null || date.isEmpty()) {
				logger.warn("Empty date provided");
				return ResponseEntity.badRequest().build();
			}

			LocalDate localDate = LocalDate.parse(date);
			List<Attendance> records = attendanceService.getAttendanceByDate(localDate);
			
			if (records.isEmpty()) {
				logger.info("No attendance records found for date: {}", date);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			logger.info("Retrieved {} attendance records for date: {}", records.size(), date);
			return ResponseEntity.ok(records);
		} catch (DateTimeParseException e) {
			logger.warn("Invalid date format provided: {}", date);
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.error("Error retrieving attendance for date: {}", date, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
