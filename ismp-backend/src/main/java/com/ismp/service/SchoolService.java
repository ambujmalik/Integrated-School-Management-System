package com.ismp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.School;
import com.ismp.repository.SchoolRepository;

@Service
public class SchoolService {

	@Autowired
    private SchoolRepository schoolRepository;

    public School createSchool(School school) {
        return schoolRepository.save(school);
    }
    
    public java.util.List<School> getAllSchools() {
        return schoolRepository.findAll();
    }
}
