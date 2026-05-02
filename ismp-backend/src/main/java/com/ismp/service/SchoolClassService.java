package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.SchoolClass;
import com.ismp.repository.SchoolClassRepository;

@Service
public class SchoolClassService {

	@Autowired
    private SchoolClassRepository schoolClassRepository;

    // Create a new class
    public SchoolClass createClass(SchoolClass schoolClass) {
        return schoolClassRepository.save(schoolClass);
    }

    // Fetch all classes
    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }
}
