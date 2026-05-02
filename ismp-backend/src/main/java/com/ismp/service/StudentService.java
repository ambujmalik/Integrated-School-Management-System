package com.ismp.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ismp.model.Student;
import com.ismp.repository.StudentRepository;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Service
public class StudentService {

	@Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // This is the method your Controller is looking for!
    public List<Student> getStudentsByClass(Integer classId) {
        return studentRepository.findByCurrentClass(classId);
    }
  

 // Inside StudentService.java
    public List<Student> getStudentsBySchool(Integer schoolId) {
        return studentRepository.findBySchool_SchoolId(schoolId);
    }
    
    public void deleteStudent(Integer id) {
        // Check if the student exists before trying to delete
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }
    public Student updateStudent(Integer id, Student details) {
        Student existingStudent = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Update the fields
        existingStudent.setFirstName(details.getFirstName());
        existingStudent.setLastName(details.getLastName());
        existingStudent.setStudentCode(details.getStudentCode());
        existingStudent.setAdmissionNumber(details.getAdmissionNumber());
        existingStudent.setDateOfBirth(details.getDateOfBirth());
        existingStudent.setGender(details.getGender());
        existingStudent.setCurrentClass(details.getCurrentClass());
        existingStudent.setAcademicYear(details.getAcademicYear());

        return studentRepository.save(existingStudent);
    }
    
    public void saveBulkStudents(MultipartFile file) throws IOException {
        List<Student> students = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        
        // Get the current date for defaults
        LocalDate today = LocalDate.now();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || formatter.formatCellValue(row.getCell(0)).isEmpty()) continue;

                Student s = new Student();
                
                // 1. Basic Info (From Excel)
                s.setFirstName(formatter.formatCellValue(row.getCell(0)));
                s.setLastName(formatter.formatCellValue(row.getCell(1)));
                s.setStudentCode(formatter.formatCellValue(row.getCell(2)));
                
                // 2. Class (From Excel)
                Cell classCell = row.getCell(3);
                s.setCurrentClass(classCell != null ? (int) classCell.getNumericCellValue() : 0);
                
                // 3. Gender (From Excel)
                s.setGender(formatter.formatCellValue(row.getCell(4)));

                // --- CRITICAL: FILLING NULLABLE=FALSE FIELDS ---
                
                // 4. Admission Number (If not in Excel, we generate one)
                String admNo = formatter.formatCellValue(row.getCell(5));
                s.setAdmissionNumber(admNo.isEmpty() ? "ADM-" + System.currentTimeMillis() + i : admNo);

                // 5. Dates (Database requires these!)
                // Using a default if Excel doesn't have a DOB column
                s.setDateOfBirth(LocalDate.of(2015, 1, 1)); 
                s.setAdmissionDate(today);

                // 6. Academic Year
                s.setAcademicYear("2025-26");
                
                // 7. Status
                s.setStatus("Active");

                students.add(s);
            }
            studentRepository.saveAll(students);
        }
    }
    
    
}
