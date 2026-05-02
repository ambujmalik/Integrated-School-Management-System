package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismp.model.TeacherSubjectAllocation;

public interface TeacherSubjectAllocationRepository extends JpaRepository<TeacherSubjectAllocation, Integer> {

	List<TeacherSubjectAllocation> findByTeacherTeacherId(Integer teacherId);
}
