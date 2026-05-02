package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.ExamResult;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Integer> {

	// Find results for a specific student
    List<ExamResult> findByStudent_StudentId(Integer studentId);
    
    // Find results for a specific exam (e.g., all class results for 'Final Exam')
    List<ExamResult> findByExam_ExamId(Integer examId);
}
