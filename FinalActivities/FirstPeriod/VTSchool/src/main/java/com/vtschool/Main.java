package com.vtschool;

import com.vtschool.service.EnrollmentService;
import com.vtschool.service.StudentService;
import com.vtschool.util.Constants;
import com.vtschool.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        try {
            // If no arguments provided, show help
            if (args.length == 0) {
                showHelp();
                return;
            }
            
            // Parse command line arguments
            String option = args[0];
            
            switch (option) {
                case Constants.OPTION_HELP_SHORT:
                case Constants.OPTION_HELP_LONG:
                    showHelp();
                    break;
                    
                case Constants.OPTION_ADD_SHORT:
                case Constants.OPTION_ADD_LONG:
                    handleAddStudents(args);
                    break;
                    
                case Constants.OPTION_ENROLL_SHORT:
                case Constants.OPTION_ENROLL_LONG:
                    handleEnroll(args);
                    break;
                    
                case Constants.OPTION_QUALIFY_SHORT:
                case Constants.OPTION_QUALIFY_LONG:
                    handleQualify(args);
                    break;
                    
                case Constants.OPTION_PRINT_SHORT:
                case Constants.OPTION_PRINT_LONG:
                    handlePrint(args);
                    break;
                    
                default:
                    System.err.println("Error: Unknown option '" + option + "'");
                    System.err.println("Use -h or --help for usage information");
                    System.exit(1);
            }
            
        } catch (Exception e) {
            logger.error("Application error: {}", e.getMessage(), e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } finally {
            // Shutdown Hibernate SessionFactory
            HibernateUtil.shutdown();
        }
    }
    
    /**
     * Displays help information
     */
    private static void showHelp() {
        System.out.println("VTSchool - Vocational Training School Management System");
        System.out.println();
        System.out.println("Usage: java -jar vtschool.jar [OPTION] [ARGUMENTS]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -h, --help              Show this help message");
        System.out.println("  -a, --add <file.xml>    Add students from XML file");
        System.out.println("  -e, --enroll <idcard> <course_code> <year>");
        System.out.println("                          Enroll a student in a course");
        System.out.println("  -q, --qualify <idcard> <course_code> <year>");
        System.out.println("                          Enter scores for a student");
        System.out.println("  -p, --print <idcard> <course_code> <year>");
        System.out.println("                          Print student results");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar vtschool.jar --add students.xml");
        System.out.println("  java -jar vtschool.jar --enroll 12332001 1 2023");
        System.out.println("  java -jar vtschool.jar --qualify 12332001 1 2023");
        System.out.println("  java -jar vtschool.jar --print 12332001 1 2023");
        System.out.println();
    }
    
    /**
     * Handles the --add option to add students from XML
     */
    private static void handleAddStudents(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Missing XML file argument");
            System.err.println("Usage: java -jar vtschool.jar --add <file.xml>");
            System.exit(1);
        }
        
        String xmlFilePath = args[1];
        logger.info("Adding students from XML file: {}", xmlFilePath);
        
        StudentService studentService = new StudentService();
        StudentService.ServiceResult result = studentService.addStudentsFromXML(xmlFilePath);
        
        if (result.isSuccess()) {
            System.out.println(result.getMessage());
            logger.info("Students added successfully");
        } else {
            System.err.println(result.getMessage());
            logger.error("Failed to add students: {}", result.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Handles the --enroll option
     */
    private static void handleEnroll(String[] args) {
        if (args.length < 4) {
            System.err.println("Error: Missing arguments");
            System.err.println("Usage: java -jar vtschool.jar --enroll <idcard> <course_code> <year>");
            System.exit(1);
        }
        
        String idCard = args[1];
        Integer courseCode;
        Integer year;
        
        try {
            courseCode = Integer.parseInt(args[2]);
            year = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Error: Course code and year must be valid integers");
            System.exit(1);
            return;
        }
        
        logger.info("Enrolling student {} in course {} for year {}", idCard, courseCode, year);
        
        EnrollmentService enrollmentService = new EnrollmentService();
        EnrollmentService.ServiceResult result = enrollmentService.enrollStudent(idCard, courseCode, year);
        
        if (result.isSuccess()) {
            System.out.println(result.getMessage());
            logger.info("Student enrolled successfully");
        } else {
            System.err.println(result.getMessage());
            logger.error("Failed to enroll student: {}", result.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Handles the --qualify option (to be implemented)
     */
    private static void handleQualify(String[] args) {
        System.out.println("Qualify functionality - To be implemented for Nov 30, 2025");
        // TODO: Implement qualification functionality
    }
    
    /**
     * Handles the --print option (to be implemented)
     */
    private static void handlePrint(String[] args) {
        System.out.println("Print functionality - To be implemented for Nov 30, 2025");
        // TODO: Implement print functionality
    }
}
