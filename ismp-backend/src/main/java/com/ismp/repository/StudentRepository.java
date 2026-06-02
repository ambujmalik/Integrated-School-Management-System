package com.ismp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ismp.model.Student;

/**
 * Repository interface for Student entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Finds all students belonging to a specific school.
     *
     * @param schoolId the school identifier
     * @return list of students in the school
     */
    List<Student> findBySchool_SchoolId(Integer schoolId);

    /**
     * Finds all students in a specific class.
     *
     * @param currentClass the class identifier
     * @return list of students in the class
     */
    List<Student> findByCurrentClass(Integer currentClass);

    /**
     * Finds a student by admission number.
     *
     * @param admissionNumber the admission number
     * @return optional containing the student if found
     */
    Optional<Student> findByAdmissionNumber(String admissionNumber);

    /**
     * Finds a student by student code.
     *
     * @param studentCode the student code
     * @return optional containing the student if found
     */
    Optional<Student> findByStudentCode(String studentCode);

    /**
     * Finds all students by academic year.
     *
     * @param academicYear the academic year
     * @return list of students in the academic year
     */
    List<Student> findByAcademicYear(String academicYear);

    /**
     * Finds all active students in a class.
     *
     * @param classId the class identifier
     * @param status the student status (Active/Inactive)
     * @return list of active students in the class
     */
    @Query("SELECT s FROM Student s WHERE s.currentClass = :classId AND s.status = :status")
    List<Student> findActiveStudentsByClass(@Param("classId") Integer classId, @Param("status") String status);

    /**
     * Counts students in a specific class.
     *
     * @param classId the class identifier
     * @return count of students in the class
     */
    long countByCurrentClass(Integer classId);

    /**
     * Checks if a student code already exists.
     *
     * @param studentCode the student code
     * @return true if student code exists, false otherwise
     */
    boolean existsByStudentCode(String studentCode);

    /**
     * Checks if an admission number already exists.
     *
     * @param admissionNumber the admission number
     * @return true if admission number exists, false otherwise
     */
    boolean existsByAdmissionNumber(String admissionNumber);
}
