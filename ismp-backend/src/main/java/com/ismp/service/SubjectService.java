package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.Subject;
import com.ismp.repository.SubjectRepository;

@Service
public class SubjectService {

	@Autowired
    private SubjectRepository subjectRepository;

    // 1. Save a new subject to the database
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    // 2. Fetch all subjects from the database
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
}
