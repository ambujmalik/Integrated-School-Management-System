package com.ismp.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ismp.exception.ResourceNotFoundException;
import com.ismp.exception.ValidationException;
import com.ismp.model.Student;
import com.ismp.repository.StudentRepository;

/**
 * Service layer for Student entity.
 * Handles business logic for student management including CRUD operations
 * and bulk import functionality.
 */
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private static final String STUDENT_NOT_FOUND_MSG = "Student not found with id: ";
    private static final String DEFAULT_ACADEMIC_YEAR = "2025-26";
    private static final String ACTIVE_STATUS = "Active";

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Creates a new student record.
     *
     * @param student the student object to create
     * @return the created student
     * @throws ValidationException if student data is invalid
     */
    public Student createStudent(Student student) {
        validateStudent(student);
        logger.info("Creating student with admission number: {}", student.getAdmissionNumber());
        return studentRepository.save(student);
    }

    /**
     * Retrieves all students from the database.
     *
     * @return list of all students
     */
    public List<Student> getAllStudents() {
        logger.debug("Fetching all students");
        return studentRepository.findAll();
    }

    /**
     * Retrieves students by their class ID.
     *
     * @param classId the class identifier
     * @return list of students in the specified class
     * @throws ValidationException if classId is invalid
     */
    public List<Student> getStudentsByClass(Integer classId) {
        if (classId == null || classId <= 0) {
            logger.warn("Invalid classId provided: {}", classId);
            throw new ValidationException("Class ID must be greater than 0");
        }
        logger.info("Fetching students for classId: {}", classId);
        return studentRepository.findByCurrentClass(classId);
    }

    /**
     * Retrieves students by their school ID.
     *
     * @param schoolId the school identifier
     * @return list of students in the specified school
     * @throws ValidationException if schoolId is invalid
     */
    public List<Student> getStudentsBySchool(Integer schoolId) {
        if (schoolId == null || schoolId <= 0) {
            logger.warn("Invalid schoolId provided: {}", schoolId);
            throw new ValidationException("School ID must be greater than 0");
        }
        logger.info("Fetching students for schoolId: {}", schoolId);
        return studentRepository.findBySchool_SchoolId(schoolId);
    }

    /**
     * Updates an existing student record.
     *
     * @param id the student ID
     * @param details the updated student details
     * @return the updated student
     * @throws ResourceNotFoundException if student is not found
     * @throws ValidationException if updated data is invalid
     */
    public Student updateStudent(Integer id, Student details) {
        if (id == null || id <= 0) {
            logger.warn("Invalid student id provided: {}", id);
            throw new ValidationException("Student ID must be greater than 0");
        }

        logger.info("Updating student with id: {}", id);
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND_MSG + id));

        validateStudent(details);

        existingStudent.setFirstName(details.getFirstName());
        existingStudent.setLastName(details.getLastName());
        existingStudent.setStudentCode(details.getStudentCode());
        existingStudent.setAdmissionNumber(details.getAdmissionNumber());
        existingStudent.setDateOfBirth(details.getDateOfBirth());
        existingStudent.setGender(details.getGender());
        existingStudent.setCurrentClass(details.getCurrentClass());
        existingStudent.setAcademicYear(details.getAcademicYear());

        logger.info("Student updated successfully with id: {}", id);
        return studentRepository.save(existingStudent);
    }

    /**
     * Deletes a student record.
     *
     * @param id the student ID to delete
     * @throws ResourceNotFoundException if student is not found
     * @throws ValidationException if id is invalid
     */
    public void deleteStudent(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid student id provided for deletion: {}", id);
            throw new ValidationException("Student ID must be greater than 0");
        }

        logger.info("Deleting student with id: {}", id);
        if (!studentRepository.existsById(id)) {
            logger.error("Student not found for deletion with id: {}", id);
            throw new ResourceNotFoundException(STUDENT_NOT_FOUND_MSG + id);
        }

        studentRepository.deleteById(id);
        logger.info("Student deleted successfully with id: {}", id);
    }

    /**
     * Bulk imports students from an Excel file.
     * Supports .xlsx format. Handles missing required fields with defaults.
     *
     * @param file the Excel file containing student data
     * @throws IOException if file reading fails
     * @throws ValidationException if file is invalid or empty
     */
    public void saveBulkStudents(MultipartFile file) throws IOException {
        validateFile(file);

        logger.info("Starting bulk import from file: {}", file.getOriginalFilename());

        List<Student> students = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        LocalDate today = LocalDate.now();
        int rowCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getLastRowNum() <= 0) {
                logger.warn("Excel file is empty");
                throw new ValidationException("Excel file contains no student records");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Skip empty rows
                if (row == null || isRowEmpty(row, formatter)) {
                    logger.debug("Skipping empty row: {}", i);
                    continue;
                }

                try {
                    Student student = parseStudentFromRow(row, formatter, today, i);
                    students.add(student);
                    rowCount++;
                } catch (Exception e) {
                    logger.error("Error parsing student at row {}: {}", i, e.getMessage());
                    // Continue processing other rows
                }
            }

            if (students.isEmpty()) {
                logger.warn("No valid student records found in file");
                throw new ValidationException("No valid student records found in the Excel file");
            }

            studentRepository.saveAll(students);
            logger.info("Successfully imported {} students from file: {}", rowCount, file.getOriginalFilename());

        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", e.getMessage());
            throw new ValidationException("Failed to process Excel file: " + e.getMessage());
        }
    }

    /**
     * Parses a single row from Excel and creates a Student object.
     *
     * @param row the Excel row
     * @param formatter the data formatter
     * @param today the current date
     * @param rowIndex the row index (for generating unique IDs)
     * @return the parsed Student object
     */
    private Student parseStudentFromRow(Row row, DataFormatter formatter, LocalDate today, int rowIndex) {
        Student student = new Student();

        // Basic Info
        String firstName = formatter.formatCellValue(row.getCell(0)).trim();
        String lastName = formatter.formatCellValue(row.getCell(1)).trim();
        String studentCode = formatter.formatCellValue(row.getCell(2)).trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new ValidationException("First name and last name are required at row " + rowIndex);
        }

        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setStudentCode(studentCode.isEmpty() ? generateStudentCode() : studentCode);

        // Class
        Cell classCell = row.getCell(3);
        int classId = (classCell != null) ? (int) classCell.getNumericCellValue() : 0;
        if (classId <= 0) {
            logger.warn("Invalid class ID at row {}, setting to 0", rowIndex);
        }
        student.setCurrentClass(classId);

        // Gender
        String gender = formatter.formatCellValue(row.getCell(4)).trim();
        student.setGender(gender.isEmpty() ? "Not Specified" : gender);

        // Admission Number
        String admNo = formatter.formatCellValue(row.getCell(5)).trim();
        student.setAdmissionNumber(admNo.isEmpty() ? generateAdmissionNumber(rowIndex) : admNo);

        // Dates and other required fields
        student.setDateOfBirth(LocalDate.of(2015, 1, 1));
        student.setAdmissionDate(today);
        student.setAcademicYear(DEFAULT_ACADEMIC_YEAR);
        student.setStatus(ACTIVE_STATUS);

        return student;
    }

    /**
     * Checks if a row is empty.
     *
     * @param row the Excel row
     * @param formatter the data formatter
     * @return true if row is empty, false otherwise
     */
    private boolean isRowEmpty(Row row, DataFormatter formatter) {
        return formatter.formatCellValue(row.getCell(0)).trim().isEmpty();
    }

    /**
     * Validates student object.
     *
     * @param student the student to validate
     * @throws ValidationException if validation fails
     */
    private void validateStudent(Student student) {
        if (student == null) {
            throw new ValidationException("Student object cannot be null");
        }

        if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name is required");
        }

        if (student.getLastName() == null || student.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name is required");
        }

        if (student.getAdmissionNumber() == null || student.getAdmissionNumber().trim().isEmpty()) {
            throw new ValidationException("Admission number is required");
        }
    }

    /**
     * Validates uploaded file.
     *
     * @param file the file to validate
     * @throws ValidationException if file is invalid
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            logger.warn("Empty or null file uploaded");
            throw new ValidationException("File cannot be empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".xlsx")) {
            logger.warn("Invalid file format: {}", filename);
            throw new ValidationException("Only .xlsx files are supported");
        }
    }

    /**
     * Generates a unique admission number.
     *
     * @param index the index for uniqueness
     * @return the generated admission number
     */
    private String generateAdmissionNumber(int index) {
        return "ADM-" + System.currentTimeMillis() + "-" + index;
    }

    /**
     * Generates a unique student code.
     *
     * @return the generated student code
     */
    private String generateStudentCode() {
        return "STU-" + System.currentTimeMillis();
    }
}
