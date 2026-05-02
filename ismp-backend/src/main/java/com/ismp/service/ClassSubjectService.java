package com.ismp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ismp.model.ClassSubject;
import com.ismp.repository.ClassSubjectRepository;

@Service
public class ClassSubjectService {

	@Autowired
    private ClassSubjectRepository classSubjectRepository;

    public ClassSubject assignSubjectToClass(ClassSubject classSubject) {
        // Use the new "existsMapping" method from your Repository
        boolean exists = classSubjectRepository.existsMapping(
                classSubject.getSchoolClass().getClassId(),
                classSubject.getSubject().getSubjectId(),
                classSubject.getAcademicYear()
        );

        if (exists) {
            throw new RuntimeException("This subject is already assigned to this class for this academic year.");
        }

        return classSubjectRepository.save(classSubject);
    }

    public List<ClassSubject> getSubjectsByClass(Integer classId, String year) {
        // Use the new "findByClassAndYear" method from your Repository
        return classSubjectRepository.findByClassAndYear(classId, year);
    }

}
