package com.ismp.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fee_structure")
public class FeeStructure {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer feeStructureId;

    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    @Column(name = "class_level", nullable = false)
    private Integer classLevel;

    @Column(name = "academic_year", nullable = false, length = 10)
    private String academicYear;

    @Column(name = "tuition_fee", nullable = false)
    private BigDecimal tuitionFee;

    @Column(name = "admission_fee")
    private BigDecimal admissionFee;

    @Column(name = "development_fee")
    private BigDecimal developmentFee;

    @Column(name = "library_fee")
    private BigDecimal libraryFee;

    @Column(name = "laboratory_fee")
    private BigDecimal laboratoryFee;

    @Column(name = "sports_fee")
    private BigDecimal sportsFee;

    @Column(name = "transport_fee")
    private BigDecimal transportFee;

    @Column(name = "total_annual_fee", nullable = false)
    private BigDecimal totalAnnualFee;

    @Column(name = "due_date_month")
    private Integer dueDateMonth;

    public Integer getFeeStructureId() {
		return feeStructureId;
	}

	public void setFeeStructureId(Integer feeStructureId) {
		this.feeStructureId = feeStructureId;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public Integer getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(Integer classLevel) {
		this.classLevel = classLevel;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public BigDecimal getTuitionFee() {
		return tuitionFee;
	}

	public void setTuitionFee(BigDecimal tuitionFee) {
		this.tuitionFee = tuitionFee;
	}

	public BigDecimal getAdmissionFee() {
		return admissionFee;
	}

	public void setAdmissionFee(BigDecimal admissionFee) {
		this.admissionFee = admissionFee;
	}

	public BigDecimal getDevelopmentFee() {
		return developmentFee;
	}

	public void setDevelopmentFee(BigDecimal developmentFee) {
		this.developmentFee = developmentFee;
	}

	public BigDecimal getLibraryFee() {
		return libraryFee;
	}

	public void setLibraryFee(BigDecimal libraryFee) {
		this.libraryFee = libraryFee;
	}

	public BigDecimal getLaboratoryFee() {
		return laboratoryFee;
	}

	public void setLaboratoryFee(BigDecimal laboratoryFee) {
		this.laboratoryFee = laboratoryFee;
	}

	public BigDecimal getSportsFee() {
		return sportsFee;
	}

	public void setSportsFee(BigDecimal sportsFee) {
		this.sportsFee = sportsFee;
	}

	public BigDecimal getTransportFee() {
		return transportFee;
	}

	public void setTransportFee(BigDecimal transportFee) {
		this.transportFee = transportFee;
	}

	public BigDecimal getTotalAnnualFee() {
		return totalAnnualFee;
	}

	public void setTotalAnnualFee(BigDecimal totalAnnualFee) {
		this.totalAnnualFee = totalAnnualFee;
	}

	public Integer getDueDateMonth() {
		return dueDateMonth;
	}

	public void setDueDateMonth(Integer dueDateMonth) {
		this.dueDateMonth = dueDateMonth;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "is_active")
    private Boolean isActive = true;
}
