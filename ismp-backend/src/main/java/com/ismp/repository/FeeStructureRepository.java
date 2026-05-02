package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.FeeStructure;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Integer> {

	// Find fee by class level and academic year
    FeeStructure findByClassLevelAndAcademicYear(Integer classLevel, String academicYear);
}
