package com.ismp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.exception.BusinessException;
import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.Assignment;
import com.ismp.repository.AssignmentRepository;

/**
 * Service layer for Assignment entity.
 * Handles business logic for assignment management.
 */
@Service
public class AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);
    private static final String ASSIGNMENT_NOT_FOUND_MSG = "Assignment not found with id: ";
    private static final String INVALID_CLASS_ID_MSG = "Class ID must be greater than 0";

    @Autowired
    private AssignmentRepository assignmentRepository;

    /**
     * Creates a new assignment.
     *
     * @param assignment the assignment object to create
     * @return the created assignment
     * @throws ValidationException if assignment data is invalid
     */
    public Assignment createAssignment(Assignment assignment) {
        validateAssignment(assignment);
        logger.info("Creating assignment: {} for class: {}", 
            assignment.getTitle(), assignment.getSchoolClass().getClassId());
        return assignmentRepository.save(assignment);
    }

    /**
     * Retrieves all assignments.
     *
     * @return list of all assignments
     */
    public List<Assignment> getAllAssignments() {
        logger.debug("Fetching all assignments");
        return assignmentRepository.findAll();
    }

    /**
     * Retrieves assignments by class ID.
     *
     * @param classId the class identifier
     * @return list of assignments for the class
     * @throws ValidationException if classId is invalid
     */
    public List<Assignment> getAssignmentsByClass(Integer classId) {
        if (classId == null || classId <= 0) {
            logger.warn("Invalid classId provided: {}", classId);
            throw new ValidationException(INVALID_CLASS_ID_MSG);
        }
        logger.info("Fetching assignments for classId: {}", classId);
        return assignmentRepository.findBySchoolClass_ClassId(classId);
    }

    /**
     * Retrieves an assignment by ID.
     *
     * @param id the assignment ID
     * @return the assignment object
     * @throws ResourceNotFoundException if assignment is not found
     * @throws ValidationException if id is invalid
     */
    public Assignment getAssignmentById(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid assignment id provided: {}", id);
            throw new ValidationException("Assignment ID must be greater than 0");
        }
        logger.info("Fetching assignment with id: {}", id);
        return assignmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Assignment not found with id: {}", id);
                    return new ResourceNotFoundException(ASSIGNMENT_NOT_FOUND_MSG + id);
                });
    }

    /**
     * Updates an existing assignment.
     *
     * @param id the assignment ID
     * @param details the updated assignment details
     * @return the updated assignment
     * @throws ResourceNotFoundException if assignment is not found
     * @throws ValidationException if updated data is invalid
     */
    public Assignment updateAssignment(Integer id, Assignment details) {
        if (id == null || id <= 0) {
            logger.warn("Invalid assignment id provided for update: {}", id);
            throw new ValidationException("Assignment ID must be greater than 0");
        }

        logger.info("Updating assignment with id: {}", id);
        Assignment existingAssignment = getAssignmentById(id);
        validateAssignment(details);

        existingAssignment.setTitle(details.getTitle());
        existingAssignment.setDescription(details.getDescription());
        existingAssignment.setDueDate(details.getDueDate());
        existingAssignment.setAssignedDate(details.getAssignedDate());
        existingAssignment.setTotalMarks(details.getTotalMarks());

        logger.info("Assignment updated successfully with id: {}", id);
        return assignmentRepository.save(existingAssignment);
    }

    /**
     * Deletes an assignment.
     *
     * @param id the assignment ID to delete
     * @throws ResourceNotFoundException if assignment is not found
     * @throws ValidationException if id is invalid
     */
    public void deleteAssignment(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid assignment id provided for deletion: {}", id);
            throw new ValidationException("Assignment ID must be greater than 0");
        }

        logger.info("Deleting assignment with id: {}", id);
        if (!assignmentRepository.existsById(id)) {
            logger.error("Assignment not found for deletion with id: {}", id);
            throw new ResourceNotFoundException(ASSIGNMENT_NOT_FOUND_MSG + id);
        }

        assignmentRepository.deleteById(id);
        logger.info("Assignment deleted successfully with id: {}", id);
    }

    /**
     * Validates assignment object.
     *
     * @param assignment the assignment to validate
     * @throws ValidationException if validation fails
     */
    private void validateAssignment(Assignment assignment) {
        if (assignment == null) {
            throw new ValidationException("Assignment object cannot be null");
        }

        if (assignment.getTitle() == null || assignment.getTitle().trim().isEmpty()) {
            throw new ValidationException("Assignment title is required");
        }

        if (assignment.getSchoolClass() == null || assignment.getSchoolClass().getClassId() == null) {
            throw new ValidationException("Class information is required");
        }

        if (assignment.getDueDate() == null) {
            throw new ValidationException("Due date is required");
        }

        if (assignment.getTotalMarks() == null || assignment.getTotalMarks() <= 0) {
            throw new ValidationException("Total marks must be greater than 0");
        }
    }
}
