package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer>{

	// Find all exams for a specific academic year (e.g., "2025-26")
    List<Exam> findByAcademicYear(String academicYear);

    // Find exams filtered by class level
    List<Exam> findByClassLevel(Integer classLevel);

    // Find only active exams
    List<Exam> findByIsActiveTrue();
}
