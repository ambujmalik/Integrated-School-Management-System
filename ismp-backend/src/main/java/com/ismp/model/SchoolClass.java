package com.ismp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
//We use the exact table name from your DB, and add the unique rule your schema requires!
@Table(name = "classes", uniqueConstraints = {
 @UniqueConstraint(columnNames = {"school_id", "class_name", "academic_year"})
})
public class SchoolClass {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "class_name", nullable = false, length = 20)
    private String className;

    @Column(name = "class_level", nullable = false)
    private Integer classLevel;

    @Column(name = "section", nullable = false, length = 5)
    private String section;

    @Column(name = "academic_year", nullable = false, length = 10)
    private String academicYear;

    @Column(name = "max_students")
    private Integer maxStudents = 40;

    @Column(name = "current_students")
    private Integer currentStudents = 0;

    @Column(name = "classroom_number", length = 10)
    private String classroomNumber;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // =========================================================
    // THE RELATIONAL MAGIC (FOREIGN KEYS)
    // =========================================================
    
    // TODO: We will uncomment these @ManyToOne blocks as soon as we build the School and Teacher models!
    
    
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private Teacher classTeacher;
    

   
    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public Integer getClassId() { return classId; }
    public void setClassId(Integer classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Integer getClassLevel() { return classLevel; }
    public void setClassLevel(Integer classLevel) { this.classLevel = classLevel; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }

    public Integer getCurrentStudents() { return currentStudents; }
    public void setCurrentStudents(Integer currentStudents) { this.currentStudents = currentStudents; }

    public String getClassroomNumber() { return classroomNumber; }
    public void setClassroomNumber(String classroomNumber) { this.classroomNumber = classroomNumber; }

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public Teacher getClassTeacher() { return classTeacher; }
    public void setClassTeacher(Teacher classTeacher) { this.classTeacher = classTeacher; }
}
