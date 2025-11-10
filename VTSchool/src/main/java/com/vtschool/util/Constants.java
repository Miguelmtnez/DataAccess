package com.vtschool.util;

public class Constants {
    
    // CLI Options
    public static final String OPTION_HELP_SHORT = "-h";
    public static final String OPTION_HELP_LONG = "--help";
    public static final String OPTION_ADD_SHORT = "-a";
    public static final String OPTION_ADD_LONG = "--add";
    public static final String OPTION_ENROLL_SHORT = "-e";
    public static final String OPTION_ENROLL_LONG = "--enroll";
    public static final String OPTION_QUALIFY_SHORT = "-q";
    public static final String OPTION_QUALIFY_LONG = "--qualify";
    public static final String OPTION_PRINT_SHORT = "-p";
    public static final String OPTION_PRINT_LONG = "--print";
    
    // Validation
    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 10;
    public static final int PASS_SCORE = 5;
    public static final int SKIP_SCORE = 99;
    
    // Phone validation regex (12 digits max)
    public static final String PHONE_REGEX = "^\\d{0,12}$";
    
    // ID Card validation (8 characters)
    public static final String IDCARD_REGEX = "^\\d{8}$";
    
    // Email validation regex
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    // Error messages
    public static final String ERROR_DUPLICATE_IDCARD = "Error: Duplicate ID card found in XML file";
    public static final String ERROR_INVALID_PHONE = "Error: Invalid phone format";
    public static final String ERROR_INVALID_EMAIL = "Error: Invalid email format";
    public static final String ERROR_INVALID_IDCARD = "Error: Invalid ID card format (must be 8 digits)";
    public static final String ERROR_FILE_NOT_FOUND = "Error: File not found";
    public static final String ERROR_STUDENT_EXISTS = "Error: Student with this ID card already exists in database";
    public static final String ERROR_XML_PARSE = "Error: Could not parse XML file";
    public static final String ERROR_NO_STUDENTS = "Error: No students found in XML file";
    
    // Success messages
    public static final String SUCCESS_STUDENTS_ADDED = "Students added successfully";
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}
