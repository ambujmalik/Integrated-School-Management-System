package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.ExamResult;
import com.ismp.repository.ExamResultRepository;

@Service
public class ExamResultService {

	@Autowired
    private ExamResultRepository examResultRepository;

    public ExamResult saveResult(ExamResult result) {
        // You could add logic here to calculate the grade automatically based on marks
        return examResultRepository.save(result);
    }

    public List<ExamResult> getResultsByStudent(Integer studentId) {
        return examResultRepository.findByStudent_StudentId(studentId);
    }
}
