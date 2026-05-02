package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

	List<Assignment> findBySchoolClass_ClassId(Integer classId);
}
