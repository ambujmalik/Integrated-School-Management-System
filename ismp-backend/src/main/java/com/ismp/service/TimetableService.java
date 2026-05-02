package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.Timetable;
import com.ismp.model.TimetableSlot;
import com.ismp.repository.TimetableRepository;
import com.ismp.repository.TimetableSlotRepository;

import jakarta.transaction.Transactional;

@Service
public class TimetableService {

	@Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private TimetableSlotRepository slotRepository;

    @Transactional
    public Timetable createFullTimetable(Timetable timetable) {
        if (timetable.getSlots() != null) {
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
                        throw new RuntimeException("Teacher conflict detected for Teacher ID: " + 
                            slot.getTeacher().getTeacherId() + " on " + slot.getDayOfWeek() + " Period " + slot.getPeriodNumber());
                    }
                }
            }
        }
        return timetableRepository.save(timetable);
    }

    public List<Timetable> getActiveTimetableByClass(Integer classId) {
        return timetableRepository.findBySchoolClassClassIdAndIsActiveTrue(classId);
    }
}
