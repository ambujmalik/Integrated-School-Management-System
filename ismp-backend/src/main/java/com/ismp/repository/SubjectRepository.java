package com.ismp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ismp.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	// You can add custom queries if needed, e.g.:
   // List<Subject> findByTeacherId(Long teacherId);
    


}
