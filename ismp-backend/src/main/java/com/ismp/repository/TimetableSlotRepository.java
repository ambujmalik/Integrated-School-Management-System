package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.TimetableSlot;

@Repository
public interface TimetableSlotRepository extends JpaRepository<TimetableSlot, Integer> {

	// Check if a teacher is already busy on a specific day and time slot
    boolean existsByTeacherTeacherIdAndDayOfWeekAndPeriodNumber(Integer teacherId, String dayOfWeek, Integer periodNumber);
}
