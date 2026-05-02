package com.ismp.model;

import java.time.LocalDateTime;

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
@Table(name = "class_subjects", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"class_id", "subject_id", "academic_year"})
})
public class ClassSubject {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classSubjectId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "academic_year", nullable = false, length = 10)
    private String academicYear;

    @Column(name = "is_compulsory")
    private Boolean isCompulsory = true;

    @Column(name = "total_marks")
    private Integer totalMarks = 100;

    @Column(name = "passing_marks")
    private Integer passingMarks = 35;

    @Column(name = "weekly_periods")
    private Integer weeklyPeriods;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

	public Integer getClassSubjectId() {
		return classSubjectId;
	}

	public void setClassSubjectId(Integer classSubjectId) {
		this.classSubjectId = classSubjectId;
	}

	public SchoolClass getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public Boolean getIsCompulsory() {
		return isCompulsory;
	}

	public void setIsCompulsory(Boolean isCompulsory) {
		this.isCompulsory = isCompulsory;
	}

	public Integer getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
	}

	public Integer getPassingMarks() {
		return passingMarks;
	}

	public void setPassingMarks(Integer passingMarks) {
		this.passingMarks = passingMarks;
	}

	public Integer getWeeklyPeriods() {
		return weeklyPeriods;
	}

	public void setWeeklyPeriods(Integer weeklyPeriods) {
		this.weeklyPeriods = weeklyPeriods;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
