package com.ismp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.Teacher;
import com.ismp.repository.TeacherRepository;

@Service
public class TeacherService {

	@Autowired
    private TeacherRepository teacherRepository;

    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    
    public java.util.List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
}
