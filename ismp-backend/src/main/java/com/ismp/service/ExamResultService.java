package com.ismp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.ExamResult;
import com.ismp.repository.ExamResultRepository;

/**
 * Service layer for ExamResult entity.
 * Handles business logic for exam result management including grade calculation.
 */
@Service
public class ExamResultService {

    private static final Logger logger = LoggerFactory.getLogger(ExamResultService.class);
    private static final String RESULT_NOT_FOUND_MSG = "Exam result not found with id: ";
    private static final String INVALID_STUDENT_ID_MSG = "Student ID must be greater than 0";
    private static final int MIN_MARKS = 0;
    private static final int MAX_MARKS = 100;

    @Autowired
    private ExamResultRepository examResultRepository;

    /**
     * Saves an exam result with automatic grade calculation.
     *
     * @param result the exam result object
     * @return the saved exam result
     * @throws ValidationException if result data is invalid
     */
    public ExamResult saveResult(ExamResult result) {
        validateExamResult(result);
        
        // Calculate and set grade based on marks
        String grade = calculateGrade(result.getMarksObtained());
        result.setGrade(grade);
        
        logger.info("Saving exam result for student: {} with marks: {} and grade: {}", 
            result.getStudent().getStudentId(), result.getMarksObtained(), grade);
        
        return examResultRepository.save(result);
    }

    /**
     * Retrieves exam results by student ID.
     *
     * @param studentId the student identifier
     * @return list of exam results for the student
     * @throws ValidationException if studentId is invalid
     */
    public List<ExamResult> getResultsByStudent(Integer studentId) {
        if (studentId == null || studentId <= 0) {
            logger.warn("Invalid studentId provided: {}", studentId);
            throw new ValidationException(INVALID_STUDENT_ID_MSG);
        }
        logger.info("Fetching exam results for studentId: {}", studentId);
        return examResultRepository.findByStudent_StudentId(studentId);
    }

    /**
     * Retrieves an exam result by ID.
     *
     * @param id the exam result ID
     * @return the exam result object
     * @throws ResourceNotFoundException if result is not found
     * @throws ValidationException if id is invalid
     */
    public ExamResult getResultById(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid exam result id provided: {}", id);
            throw new ValidationException("Exam Result ID must be greater than 0");
        }
        logger.info("Fetching exam result with id: {}", id);
        return examResultRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Exam result not found with id: {}", id);
                    return new ResourceNotFoundException(RESULT_NOT_FOUND_MSG + id);
                });
    }

    /**
     * Updates an existing exam result.
     *
     * @param id the exam result ID
     * @param details the updated exam result details
     * @return the updated exam result
     * @throws ResourceNotFoundException if result is not found
     * @throws ValidationException if updated data is invalid
     */
    public ExamResult updateResult(Integer id, ExamResult details) {
        if (id == null || id <= 0) {
            logger.warn("Invalid exam result id provided for update: {}", id);
            throw new ValidationException("Exam Result ID must be greater than 0");
        }

        logger.info("Updating exam result with id: {}", id);
        ExamResult existingResult = getResultById(id);
        validateExamResult(details);

        existingResult.setMarksObtained(details.getMarksObtained());
        String grade = calculateGrade(details.getMarksObtained());
        existingResult.setGrade(grade);
        existingResult.setExam(details.getExam());
        existingResult.setSubject(details.getSubject());

        logger.info("Exam result updated successfully with id: {}", id);
        return examResultRepository.save(existingResult);
    }

    /**
     * Deletes an exam result.
     *
     * @param id the exam result ID to delete
     * @throws ResourceNotFoundException if result is not found
     * @throws ValidationException if id is invalid
     */
    public void deleteResult(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid exam result id provided for deletion: {}", id);
            throw new ValidationException("Exam Result ID must be greater than 0");
        }

        logger.info("Deleting exam result with id: {}", id);
        if (!examResultRepository.existsById(id)) {
            logger.error("Exam result not found for deletion with id: {}", id);
            throw new ResourceNotFoundException(RESULT_NOT_FOUND_MSG + id);
        }

        examResultRepository.deleteById(id);
        logger.info("Exam result deleted successfully with id: {}", id);
    }

    /**
     * Calculates the grade based on marks obtained.
     * Grade Scale: A (90-100), B (80-89), C (70-79), D (60-69), F (<60)
     *
     * @param marks the marks obtained
     * @return the calculated grade
     */
    private String calculateGrade(Double marks) {
        if (marks == null) {
            return "F";
        }

        if (marks >= 90) {
            return "A";
        } else if (marks >= 80) {
            return "B";
        } else if (marks >= 70) {
            return "C";
        } else if (marks >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    /**
     * Validates exam result object.
     *
     * @param result the exam result to validate
     * @throws ValidationException if validation fails
     */
    private void validateExamResult(ExamResult result) {
        if (result == null) {
            throw new ValidationException("Exam result object cannot be null");
        }

        if (result.getStudent() == null || result.getStudent().getStudentId() == null) {
            throw new ValidationException("Student information is required");
        }

        if (result.getExam() == null || result.getExam().getExamId() == null) {
            throw new ValidationException("Exam information is required");
        }

        if (result.getMarksObtained() == null) {
            throw new ValidationException("Marks obtained is required");
        }

        if (result.getMarksObtained() < MIN_MARKS || result.getMarksObtained() > MAX_MARKS) {
            throw new ValidationException("Marks must be between " + MIN_MARKS + " and " + MAX_MARKS);
        }
    }
}
