package com.ismp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.Teacher;
import com.ismp.repository.TeacherRepository;

/**
 * Service layer for Teacher entity.
 * Handles business logic for teacher management including CRUD operations.
 */
@Service
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);
    private static final String TEACHER_NOT_FOUND_MSG = "Teacher not found with id: ";

    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * Creates a new teacher record.
     *
     * @param teacher the teacher object to create
     * @return the created teacher
     * @throws ValidationException if teacher data is invalid
     */
    public Teacher createTeacher(Teacher teacher) {
        validateTeacher(teacher);
        logger.info("Creating teacher with name: {} {}", teacher.getFirstName(), teacher.getLastName());
        return teacherRepository.save(teacher);
    }

    /**
     * Retrieves all teachers from the database.
     *
     * @return list of all teachers
     */
    public List<Teacher> getAllTeachers() {
        logger.debug("Fetching all teachers");
        return teacherRepository.findAll();
    }

    /**
     * Retrieves a teacher by ID.
     *
     * @param id the teacher ID
     * @return the teacher object
     * @throws ResourceNotFoundException if teacher is not found
     * @throws ValidationException if id is invalid
     */
    public Teacher getTeacherById(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid teacher id provided: {}", id);
            throw new ValidationException("Teacher ID must be greater than 0");
        }
        logger.info("Fetching teacher with id: {}", id);
        return teacherRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Teacher not found with id: {}", id);
                    return new ResourceNotFoundException(TEACHER_NOT_FOUND_MSG + id);
                });
    }

    /**
     * Updates an existing teacher record.
     *
     * @param id the teacher ID
     * @param details the updated teacher details
     * @return the updated teacher
     * @throws ResourceNotFoundException if teacher is not found
     * @throws ValidationException if updated data is invalid
     */
    public Teacher updateTeacher(Integer id, Teacher details) {
        if (id == null || id <= 0) {
            logger.warn("Invalid teacher id provided for update: {}", id);
            throw new ValidationException("Teacher ID must be greater than 0");
        }

        logger.info("Updating teacher with id: {}", id);
        Teacher existingTeacher = getTeacherById(id);
        validateTeacher(details);

        existingTeacher.setFirstName(details.getFirstName());
        existingTeacher.setLastName(details.getLastName());
        existingTeacher.setEmail(details.getEmail());
        existingTeacher.setPhoneNumber(details.getPhoneNumber());
        existingTeacher.setEmployeeId(details.getEmployeeId());
        existingTeacher.setQualification(details.getQualification());
        existingTeacher.setDepartment(details.getDepartment());

        logger.info("Teacher updated successfully with id: {}", id);
        return teacherRepository.save(existingTeacher);
    }

    /**
     * Deletes a teacher record.
     *
     * @param id the teacher ID to delete
     * @throws ResourceNotFoundException if teacher is not found
     * @throws ValidationException if id is invalid
     */
    public void deleteTeacher(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid teacher id provided for deletion: {}", id);
            throw new ValidationException("Teacher ID must be greater than 0");
        }

        logger.info("Deleting teacher with id: {}", id);
        if (!teacherRepository.existsById(id)) {
            logger.error("Teacher not found for deletion with id: {}", id);
            throw new ResourceNotFoundException(TEACHER_NOT_FOUND_MSG + id);
        }

        teacherRepository.deleteById(id);
        logger.info("Teacher deleted successfully with id: {}", id);
    }

    /**
     * Validates teacher object.
     *
     * @param teacher the teacher to validate
     * @throws ValidationException if validation fails
     */
    private void validateTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new ValidationException("Teacher object cannot be null");
        }

        if (teacher.getFirstName() == null || teacher.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required");
        }

        if (teacher.getLastName() == null || teacher.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required");
        }

        if (teacher.getEmployeeId() == null || teacher.getEmployeeId().trim().isEmpty()) {
            throw new ValidationException("Employee ID is required");
        }
    }
}
