package com.ismp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.exception.BusinessException;
import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.ClassSubject;
import com.ismp.repository.ClassSubjectRepository;

/**
 * Service layer for ClassSubject entity.
 * Handles business logic for assigning subjects to classes with duplicate prevention.
 */
@Service
public class ClassSubjectService {

    private static final Logger logger = LoggerFactory.getLogger(ClassSubjectService.class);
    private static final String CLASS_SUBJECT_NOT_FOUND_MSG = "Class subject mapping not found with id: ";
    private static final String DUPLICATE_MAPPING_MSG = "This subject is already assigned to this class for this academic year";
    private static final String INVALID_CLASS_ID_MSG = "Class ID must be greater than 0";

    @Autowired
    private ClassSubjectRepository classSubjectRepository;

    /**
     * Assigns a subject to a class with duplicate prevention.
     *
     * @param classSubject the class subject mapping to create
     * @return the created class subject mapping
     * @throws ValidationException if input data is invalid
     * @throws BusinessException if subject is already assigned to the class for this year
     */
    public ClassSubject assignSubjectToClass(ClassSubject classSubject) {
        validateClassSubject(classSubject);
        
        logger.info("Attempting to assign subject: {} to class: {} for year: {}", 
            classSubject.getSubject().getSubjectId(),
            classSubject.getSchoolClass().getClassId(),
            classSubject.getAcademicYear());

        // Check for duplicate mapping
        boolean exists = classSubjectRepository.existsMapping(
                classSubject.getSchoolClass().getClassId(),
                classSubject.getSubject().getSubjectId(),
                classSubject.getAcademicYear()
        );

        if (exists) {
            logger.warn("Duplicate subject assignment attempt: {} to class: {}", 
                classSubject.getSubject().getSubjectId(),
                classSubject.getSchoolClass().getClassId());
            throw new BusinessException(DUPLICATE_MAPPING_MSG);
        }

        ClassSubject savedMapping = classSubjectRepository.save(classSubject);
        logger.info("Subject successfully assigned to class for year: {}", classSubject.getAcademicYear());
        return savedMapping;
    }

    /**
     * Retrieves subjects assigned to a class for a specific academic year.
     *
     * @param classId the class identifier
     * @param year the academic year
     * @return list of class subject mappings
     * @throws ValidationException if classId or year is invalid
     */
    public List<ClassSubject> getSubjectsByClass(Integer classId, String year) {
        if (classId == null || classId <= 0) {
            logger.warn("Invalid classId provided: {}", classId);
            throw new ValidationException(INVALID_CLASS_ID_MSG);
        }
        if (year == null || year.trim().isEmpty()) {
            logger.warn("Invalid academic year provided");
            throw new ValidationException("Academic year cannot be null or empty");
        }
        logger.info("Fetching subjects for classId: {} and year: {}", classId, year);
        return classSubjectRepository.findByClassAndYear(classId, year);
    }

    /**
     * Retrieves a class subject mapping by ID.
     *
     * @param id the class subject ID
     * @return the class subject object
     * @throws ResourceNotFoundException if mapping is not found
     * @throws ValidationException if id is invalid
     */
    public ClassSubject getClassSubjectById(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid class subject id provided: {}", id);
            throw new ValidationException("Class Subject ID must be greater than 0");
        }
        logger.info("Fetching class subject with id: {}", id);
        return classSubjectRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Class subject not found with id: {}", id);
                    return new ResourceNotFoundException(CLASS_SUBJECT_NOT_FOUND_MSG + id);
                });
    }

    /**
     * Updates an existing class subject mapping.
     *
     * @param id the class subject ID
     * @param details the updated class subject details
     * @return the updated class subject
     * @throws ResourceNotFoundException if mapping is not found
     * @throws ValidationException if updated data is invalid
     */
    public ClassSubject updateClassSubject(Integer id, ClassSubject details) {
        if (id == null || id <= 0) {
            logger.warn("Invalid class subject id provided for update: {}", id);
            throw new ValidationException("Class Subject ID must be greater than 0");
        }

        logger.info("Updating class subject with id: {}", id);
        ClassSubject existingMapping = getClassSubjectById(id);
        validateClassSubject(details);

        existingMapping.setAcademicYear(details.getAcademicYear());

        logger.info("Class subject updated successfully with id: {}", id);
        return classSubjectRepository.save(existingMapping);
    }

    /**
     * Deletes a class subject mapping.
     *
     * @param id the class subject ID to delete
     * @throws ResourceNotFoundException if mapping is not found
     * @throws ValidationException if id is invalid
     */
    public void deleteClassSubject(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid class subject id provided for deletion: {}", id);
            throw new ValidationException("Class Subject ID must be greater than 0");
        }

        logger.info("Deleting class subject with id: {}", id);
        if (!classSubjectRepository.existsById(id)) {
            logger.error("Class subject not found for deletion with id: {}", id);
            throw new ResourceNotFoundException(CLASS_SUBJECT_NOT_FOUND_MSG + id);
        }

        classSubjectRepository.deleteById(id);
        logger.info("Class subject deleted successfully with id: {}", id);
    }

    /**
     * Validates class subject object.
     *
     * @param classSubject the class subject to validate
     * @throws ValidationException if validation fails
     */
    private void validateClassSubject(ClassSubject classSubject) {
        if (classSubject == null) {
            throw new ValidationException("Class subject object cannot be null");
        }

        if (classSubject.getSchoolClass() == null || classSubject.getSchoolClass().getClassId() == null) {
            throw new ValidationException("Class information is required");
        }

        if (classSubject.getSubject() == null || classSubject.getSubject().getSubjectId() == null) {
            throw new ValidationException("Subject information is required");
        }

        if (classSubject.getAcademicYear() == null || classSubject.getAcademicYear().trim().isEmpty()) {
            throw new ValidationException("Academic year is required");
        }
    }
}
