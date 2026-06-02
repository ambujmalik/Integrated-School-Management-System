package com.ismp.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.Attendance;
import com.ismp.repository.AttendanceRepository;

import jakarta.transaction.Transactional;

/**
 * Service layer for Attendance entity.
 * Handles business logic for attendance management including marking and retrieval.
 */
@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
    private static final String INVALID_DATE_MSG = "Attendance date cannot be null";
    private static final String INVALID_CLASS_ID_MSG = "Class ID must be greater than 0";

    @Autowired
    private AttendanceRepository attendanceRepository;

    /**
     * Marks attendance for a single record.
     *
     * @param attendance the attendance object to mark
     * @return the saved attendance record
     * @throws ValidationException if attendance data is invalid
     */
    public Attendance markAttendance(Attendance attendance) {
        validateAttendance(attendance);
        logger.info("Marking attendance for student: {} on date: {}", 
            attendance.getStudent().getStudentId(), attendance.getAttendanceDate());
        return attendanceRepository.save(attendance);
    }

    /**
     * Retrieves attendance records by date.
     *
     * @param date the attendance date
     * @return list of attendance records for the date
     * @throws ValidationException if date is null
     */
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        if (date == null) {
            logger.warn("Invalid date provided for attendance retrieval");
            throw new ValidationException(INVALID_DATE_MSG);
        }
        logger.info("Fetching attendance records for date: {}", date);
        return attendanceRepository.findByAttendanceDate(date);
    }

    /**
     * Retrieves attendance records by class and date.
     *
     * @param classId the class identifier
     * @param date the attendance date
     * @return list of attendance records for the class and date
     * @throws ValidationException if classId or date is invalid
     */
    public List<Attendance> getAttendanceByClassAndDate(Integer classId, LocalDate date) {
        if (classId == null || classId <= 0) {
            logger.warn("Invalid classId provided: {}", classId);
            throw new ValidationException(INVALID_CLASS_ID_MSG);
        }
        if (date == null) {
            logger.warn("Invalid date provided for attendance retrieval");
            throw new ValidationException(INVALID_DATE_MSG);
        }
        logger.info("Fetching attendance for classId: {} on date: {}", classId, date);
        return attendanceRepository.findBySchoolClass_ClassIdAndAttendanceDate(classId, date);
    }

    /**
     * Saves multiple attendance records in bulk.
     *
     * @param attendanceList the list of attendance records to save
     * @throws ValidationException if list is null or empty
     */
    public void saveAllAttendance(List<Attendance> attendanceList) {
        if (attendanceList == null || attendanceList.isEmpty()) {
            logger.warn("Empty or null attendance list provided");
            throw new ValidationException("Attendance list cannot be empty");
        }
        logger.info("Saving {} attendance records", attendanceList.size());
        attendanceRepository.saveAll(attendanceList);
        logger.info("Successfully saved {} attendance records", attendanceList.size());
    }

    /**
     * Saves attendance records for a class on a specific date, replacing existing records.
     * This is a transactional operation that deletes existing records and saves new ones.
     *
     * @param classId the class identifier
     * @param date the attendance date
     * @param attendanceList the list of attendance records to save
     * @throws ValidationException if input parameters are invalid
     */
    @Transactional
    public void saveAllAttendanceForClassAndDate(Integer classId, LocalDate date, List<Attendance> attendanceList) {
        if (classId == null || classId <= 0) {
            logger.warn("Invalid classId provided: {}", classId);
            throw new ValidationException(INVALID_CLASS_ID_MSG);
        }
        if (date == null) {
            logger.warn("Invalid date provided");
            throw new ValidationException(INVALID_DATE_MSG);
        }
        if (attendanceList == null || attendanceList.isEmpty()) {
            logger.warn("Empty or null attendance list provided");
            throw new ValidationException("Attendance list cannot be empty");
        }

        logger.info("Saving attendance for classId: {} on date: {}", classId, date);
        
        // Delete existing attendance to avoid duplicates
        int deletedCount = attendanceRepository.deleteBySchoolClass_ClassIdAndAttendanceDate(classId, date);
        logger.debug("Deleted {} existing attendance records", deletedCount);

        // Save new attendance records
        attendanceRepository.saveAll(attendanceList);
        logger.info("Successfully saved {} new attendance records", attendanceList.size());
    }

    /**
     * Validates attendance object.
     *
     * @param attendance the attendance to validate
     * @throws ValidationException if validation fails
     */
    private void validateAttendance(Attendance attendance) {
        if (attendance == null) {
            throw new ValidationException("Attendance object cannot be null");
        }

        if (attendance.getStudent() == null || attendance.getStudent().getStudentId() == null) {
            throw new ValidationException("Student information is required");
        }

        if (attendance.getSchoolClass() == null || attendance.getSchoolClass().getClassId() == null) {
            throw new ValidationException("Class information is required");
        }

        if (attendance.getAttendanceDate() == null) {
            throw new ValidationException("Attendance date is required");
        }

        if (attendance.getStatus() == null || attendance.getStatus().trim().isEmpty()) {
            throw new ValidationException("Attendance status is required (Present/Absent/Leave)");
        }
    }
}
