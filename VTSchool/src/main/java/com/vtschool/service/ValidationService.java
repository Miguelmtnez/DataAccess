package com.vtschool.service;

import com.vtschool.model.Student;
import com.vtschool.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);
    private static final Pattern PHONE_PATTERN = Pattern.compile(Constants.PHONE_REGEX);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(Constants.EMAIL_REGEX);
    private static final Pattern IDCARD_PATTERN = Pattern.compile(Constants.IDCARD_REGEX);
    
    /**
     * Validates a list of students before saving to database
     * @param students List of students to validate
     * @return ValidationResult containing validation status and error messages
     */
    public ValidationResult validateStudents(List<Student> students) {
        List<String> errors = new ArrayList<>();
        
        if (students == null || students.isEmpty()) {
            errors.add(Constants.ERROR_NO_STUDENTS);
            return new ValidationResult(false, errors);
        }
        
        // Check for duplicate ID cards within the XML file
        Set<String> idCards = new HashSet<>();
        for (Student student : students) {
            if (!idCards.add(student.getIdCard())) {
                errors.add(Constants.ERROR_DUPLICATE_IDCARD + ": " + student.getIdCard());
                logger.error("Duplicate ID card found in XML: {}", student.getIdCard());
            }
        }
        
        // Validate each student
        for (Student student : students) {
            validateStudent(student, errors);
        }
        
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    /**
     * Validates a single student
     * @param student Student to validate
     * @param errors List to add validation errors to
     */
    private void validateStudent(Student student, List<String> errors) {
        // Validate ID card
        if (student.getIdCard() == null || !IDCARD_PATTERN.matcher(student.getIdCard()).matches()) {
            errors.add(Constants.ERROR_INVALID_IDCARD + ": " + student.getIdCard());
            logger.error("Invalid ID card format: {}", student.getIdCard());
        }
        
        // Validate first name
        if (student.getFirstName() == null || student.getFirstName().isBlank()) {
            errors.add("Error: First name is required for student " + student.getIdCard());
        }
        
        // Validate last name
        if (student.getLastName() == null || student.getLastName().isBlank()) {
            errors.add("Error: Last name is required for student " + student.getIdCard());
        }
        
        // Validate phone (optional, but if present must be valid)
        if (student.getPhone() != null && !student.getPhone().isBlank()) {
            if (!PHONE_PATTERN.matcher(student.getPhone()).matches()) {
                errors.add(Constants.ERROR_INVALID_PHONE + ": " + student.getPhone() + 
                        " (student: " + student.getIdCard() + ")");
                logger.error("Invalid phone format for student {}: {}", 
                        student.getIdCard(), student.getPhone());
            }
        }
        
        // Validate email (optional, but if present must be valid)
        if (student.getEmail() != null && !student.getEmail().isBlank()) {
            if (!EMAIL_PATTERN.matcher(student.getEmail()).matches()) {
                errors.add(Constants.ERROR_INVALID_EMAIL + ": " + student.getEmail() + 
                        " (student: " + student.getIdCard() + ")");
                logger.error("Invalid email format for student {}: {}", 
                        student.getIdCard(), student.getEmail());
            }
        }
    }
    
    /**
     * Validates a phone number
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidPhone(String phone) {
        return phone == null || phone.isBlank() || PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validates an email address
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        return email == null || email.isBlank() || EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates an ID card
     * @param idCard ID card to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidIdCard(String idCard) {
        return idCard != null && IDCARD_PATTERN.matcher(idCard).matches();
    }
    
    /**
     * Inner class to hold validation results
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        
        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public String getErrorMessage() {
            return String.join("\n", errors);
        }
    }
}
