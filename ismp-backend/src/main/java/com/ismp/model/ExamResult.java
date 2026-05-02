package com.ismp.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exam_results")
public class ExamResult {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam; 

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // ADDED: This was missing and caused your SQL error
    @Column(name = "theory_marks")
    private BigDecimal theoryMarks;

    @Column(name = "practical_marks")
    private BigDecimal practicalMarks;

    @Column(name = "total_marks", nullable = false)
    private BigDecimal totalMarks;
    
    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks;

    @Column(name = "grade", length = 5)
    private String grade;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    // ==========================================
    // COMPLETE GETTERS AND SETTERS
    // ==========================================

    public Integer getResultId() { return resultId; }
    public void setResultId(Integer resultId) { this.resultId = resultId; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public BigDecimal getTheoryMarks() { return theoryMarks; }
    public void setTheoryMarks(BigDecimal theoryMarks) { this.theoryMarks = theoryMarks; }

    public BigDecimal getPracticalMarks() { return practicalMarks; }
    public void setPracticalMarks(BigDecimal practicalMarks) { this.practicalMarks = practicalMarks; }

    public BigDecimal getTotalMarks() { return totalMarks; }
    public void setTotalMarks(BigDecimal totalMarks) { this.totalMarks = totalMarks; }

    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

}
