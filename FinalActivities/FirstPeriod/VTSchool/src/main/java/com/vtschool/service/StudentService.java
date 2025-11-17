package com.vtschool.service;

import com.vtschool.dao.StudentDAO;
import com.vtschool.model.Student;
import com.vtschool.util.Constants;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StudentService {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentDAO studentDAO;
    private final XMLParserService xmlParserService;
    private final ValidationService validationService;
    
    public StudentService() {
        this.studentDAO = new StudentDAO();
        this.xmlParserService = new XMLParserService();
        this.validationService = new ValidationService();
    }
    
    /**
     * Adds students from an XML file to the database
     * All students are added in a single transaction (all or nothing)
     * @param xmlFilePath Path to the XML file
     * @return ServiceResult indicating success or failure
     */
    public ServiceResult addStudentsFromXML(String xmlFilePath) {
        try {
            // Parse students from XML
            List<Student> students = xmlParserService.parseStudentsFromXML(xmlFilePath);
            
            if (students.isEmpty()) {
                logger.warn("No students found in XML file");
                return new ServiceResult(false, Constants.ERROR_NO_STUDENTS);
            }
            
            // Validate students
            ValidationService.ValidationResult validationResult = 
                    validationService.validateStudents(students);
            
            if (!validationResult.isValid()) {
                logger.error("Validation errors found: {}", validationResult.getErrorMessage());
                return new ServiceResult(false, validationResult.getErrorMessage());
            }
            
            // Check if any student already exists in database
            for (Student student : students) {
                if (studentDAO.existsByIdCard(student.getIdCard())) {
                    String error = Constants.ERROR_STUDENT_EXISTS + ": " + student.getIdCard();
                    logger.error(error);
                    return new ServiceResult(false, error);
                }
            }
            
            // Save all students in a single transaction
            boolean success = studentDAO.saveAll(students);
            
            if (success) {
                String message = students.size() + " " + Constants.SUCCESS_STUDENTS_ADDED;
                logger.info(message);
                return new ServiceResult(true, message);
            } else {
                logger.error("Failed to save students to database");
                return new ServiceResult(false, "Error: Could not save students to database");
            }
            
        } catch (JAXBException e) {
            logger.error("XML parsing error: {}", e.getMessage());
            return new ServiceResult(false, Constants.ERROR_XML_PARSE + ": " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("File error: {}", e.getMessage());
            return new ServiceResult(false, Constants.ERROR_FILE_NOT_FOUND + ": " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return new ServiceResult(false, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Inner class to hold service operation results
     */
    public static class ServiceResult {
        private final boolean success;
        private final String message;
        
        public ServiceResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
