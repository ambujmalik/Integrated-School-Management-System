package com.ismp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.exception.BusinessException;
import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.Timetable;
import com.ismp.model.TimetableSlot;
import com.ismp.repository.TimetableRepository;
import com.ismp.repository.TimetableSlotRepository;

import jakarta.transaction.Transactional;

/**
 * Service layer for Timetable entity.
 * Handles business logic for timetable management including conflict detection.
 */
@Service
public class TimetableService {

    private static final Logger logger = LoggerFactory.getLogger(TimetableService.class);
    private static final String INVALID_CLASS_ID_MSG = "Class ID must be greater than 0";
    private static final String TEACHER_CONFLICT_MSG = "Teacher conflict detected for Teacher ID: ";

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private TimetableSlotRepository slotRepository;

    /**
     * Creates a full timetable with slots and validates for teacher conflicts.
     * This is a transactional operation.
     *
     * @param timetable the timetable object with slots
     * @return the created timetable
     * @throws ValidationException if timetable data is invalid
     * @throws BusinessException if teacher conflict is detected
     */
    @Transactional
    public Timetable createFullTimetable(Timetable timetable) {
        validateTimetable(timetable);
        logger.info("Creating timetable for class: {}", timetable.getSchoolClass().getClassId());

        if (timetable.getSlots() != null && !timetable.getSlots().isEmpty()) {
            validateTimetableSlots(timetable);
        }

        Timetable savedTimetable = timetableRepository.save(timetable);
        logger.info("Timetable created successfully with {} slots", 
            (timetable.getSlots() != null ? timetable.getSlots().size() : 0));
        return savedTimetable;
    }

    /**
     * Retrieves active timetables for a specific class.
     *
     * @param classId the class identifier
     * @return list of active timetables
     * @throws ValidationException if classId is invalid
     */
    public List<Timetable> getActiveTimetableByClass(Integer classId) {
        if (classId == null || classId <= 0) {
            logger.warn("Invalid classId provided: {}", classId);
            throw new ValidationException(INVALID_CLASS_ID_MSG);
        }
        logger.info("Fetching active timetables for classId: {}", classId);
        return timetableRepository.findBySchoolClassClassIdAndIsActiveTrue(classId);
    }

    /**
     * Retrieves a timetable by ID.
     *
     * @param id the timetable ID
     * @return the timetable object
     * @throws ResourceNotFoundException if timetable is not found
     * @throws ValidationException if id is invalid
     */
    public Timetable getTimetableById(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid timetable id provided: {}", id);
            throw new ValidationException("Timetable ID must be greater than 0");
        }
        logger.info("Fetching timetable with id: {}", id);
        return timetableRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Timetable not found with id: {}", id);
                    return new ResourceNotFoundException("Timetable not found with id: " + id);
                });
    }

    /**
     * Updates an existing timetable.
     *
     * @param id the timetable ID
     * @param details the updated timetable details
     * @return the updated timetable
     * @throws ResourceNotFoundException if timetable is not found
     * @throws ValidationException if updated data is invalid
     */
    public Timetable updateTimetable(Integer id, Timetable details) {
        if (id == null || id <= 0) {
            logger.warn("Invalid timetable id provided for update: {}", id);
            throw new ValidationException("Timetable ID must be greater than 0");
        }

        logger.info("Updating timetable with id: {}", id);
        Timetable existingTimetable = getTimetableById(id);
        validateTimetable(details);

        existingTimetable.setAcademicYear(details.getAcademicYear());
        existingTimetable.setIsActive(details.getIsActive());

        logger.info("Timetable updated successfully with id: {}", id);
        return timetableRepository.save(existingTimetable);
    }

    /**
     * Validates timetable slots for teacher conflicts.
     *
     * @param timetable the timetable containing slots
     * @throws BusinessException if a teacher conflict is detected
     */
    private void validateTimetableSlots(Timetable timetable) {
        for (TimetableSlot slot : timetable.getSlots()) {
            // Link child to parent
            slot.setTimetable(timetable);

            // Validation: Check for Teacher Overlap (Exclude breaks)
            if (Boolean.FALSE.equals(slot.getIsBreak()) && slot.getTeacher() != null) {
                boolean conflict = slotRepository.existsByTeacherTeacherIdAndDayOfWeekAndPeriodNumber(
                        slot.getTeacher().getTeacherId(),
                        slot.getDayOfWeek(),
                        slot.getPeriodNumber()
                );
                if (conflict) {
                    String errorMsg = TEACHER_CONFLICT_MSG + slot.getTeacher().getTeacherId() + 
                        " on " + slot.getDayOfWeek() + " Period " + slot.getPeriodNumber();
                    logger.error(errorMsg);
                    throw new BusinessException(errorMsg);
                }
            }
        }
    }

    /**
     * Validates timetable object.
     *
     * @param timetable the timetable to validate
     * @throws ValidationException if validation fails
     */
    private void validateTimetable(Timetable timetable) {
        if (timetable == null) {
            throw new ValidationException("Timetable object cannot be null");
        }

        if (timetable.getSchoolClass() == null || timetable.getSchoolClass().getClassId() == null) {
            throw new ValidationException("Class information is required");
        }

        if (timetable.getAcademicYear() == null || timetable.getAcademicYear().trim().isEmpty()) {
            throw new ValidationException("Academic year is required");
        }
    }
}
