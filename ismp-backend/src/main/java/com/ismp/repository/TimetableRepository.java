package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Timetable;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Integer> {

	List<Timetable> findBySchoolClassClassIdAndIsActiveTrue(Integer classId);
}
