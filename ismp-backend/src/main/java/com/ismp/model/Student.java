package com.ismp.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

    public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "student_code", unique = true, nullable = false, length = 20)
    private String studentCode;

    @Column(name = "admission_number", unique = true, nullable = false, length = 30)
    private String admissionNumber; // Matches your psql 'admission_number'

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "first_name_odia", length = 100)
    private String firstNameOdia;

    @Column(name = "last_name_odia", length = 100)
    private String lastNameOdia;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate; // Matches your psql 'admission_date'

    @Column(name = "academic_year", nullable = false, length = 10)
    private String academicYear;

    @Column(name = "status", length = 20)
    private String status = "Active";

    @Column(name = "aadhar_number", length = 12, unique = true)
    private String aadharNumber;

    // Mapping the Foreign Keys from your psql constraints
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "current_class")
    private Integer currentClass;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parent> parents;
    public List<Parent> getParents() {
		return parents;
	}
	public void setParents(List<Parent> parents) {
		this.parents = parents;
	}
	
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StudentEnrollment> enrollments;
	public List<StudentEnrollment> getEnrollments() {
		return enrollments;
	}
	public void setEnrollments(List<StudentEnrollment> enrollments) {
		this.enrollments = enrollments;
	}
	// --- GETTERS AND SETTERS ---
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getAdmissionNumber() { return admissionNumber; }
    public void setAdmissionNumber(String admissionNumber) { this.admissionNumber = admissionNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }
	public String getFirstNameOdia() {
		return firstNameOdia;
	}
	public void setFirstNameOdia(String firstNameOdia) {
		this.firstNameOdia = firstNameOdia;
	}
	public String getLastNameOdia() {
		return lastNameOdia;
	}
	public void setLastNameOdia(String lastNameOdia) {
		this.lastNameOdia = lastNameOdia;
	}
	public String getAadharNumber() {
		return aadharNumber;
	}
	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	public Integer getCurrentClass() {
		return currentClass;
	}
	public void setCurrentClass(Integer currentClass) {
		this.currentClass = currentClass;
	}
	
}
