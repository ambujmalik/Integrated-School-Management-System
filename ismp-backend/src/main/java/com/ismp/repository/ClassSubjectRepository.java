package com.ismp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ismp.model.ClassSubject;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Integer> {

	// Check if a subject is already mapped to a class to avoid duplicates
    //boolean existsBySchoolClassClassIdAndSubjectSubjectIdAndAcademicYear(Integer classId, Integer subjectId, String academicYear);
    @Query("SELECT cs FROM ClassSubject cs WHERE cs.schoolClass.classId = :classId AND cs.academicYear = :year")
    List<ClassSubject> findByClassAndYear(@Param("classId") Integer classId, @Param("year") String year);

    @Query("SELECT COUNT(cs) > 0 FROM ClassSubject cs WHERE cs.schoolClass.classId = :cId AND cs.subject.subjectId = :sId AND cs.academicYear = :year")
    boolean existsMapping(@Param("cId") Integer classId, @Param("sId") Integer subjectId, @Param("year") String year);
}
