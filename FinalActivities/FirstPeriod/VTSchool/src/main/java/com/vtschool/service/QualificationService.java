package com.vtschool.service;

import com.vtschool.dao.EnrollmentDAO;
import com.vtschool.dao.ScoreDAO;
import com.vtschool.dao.StudentDAO;
import com.vtschool.model.Enrollment;
import com.vtschool.model.Score;
import com.vtschool.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class QualificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(QualificationService.class);
    private final EnrollmentDAO enrollmentDAO;
    private final ScoreDAO scoreDAO;
    private final StudentDAO studentDAO;
    
    public QualificationService() {
        this.enrollmentDAO = new EnrollmentDAO();
        this.scoreDAO = new ScoreDAO();
        this.studentDAO = new StudentDAO();
    }
    
    /**
     * Service result class
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
    
    /**
     * Enters scores for a student's enrollment interactively
     * @param idCard Student ID card
     * @param courseCode Course code
     * @param year Enrollment year
     * @return ServiceResult with success status and message
     */
    public ServiceResult qualifyStudent(String idCard, Integer courseCode, Integer year) {
        // Validate student exists
        Student student = studentDAO.findByIdCard(idCard).orElse(null);
        if (student == null) {
            return new ServiceResult(false, "Error: Student with ID card '" + idCard + "' not found");
        }
        
        // Find enrollment
        Enrollment enrollment = enrollmentDAO.findByStudentCourseYear(idCard, courseCode, year).orElse(null);
        if (enrollment == null) {
            return new ServiceResult(false, 
                "Error: No enrollment found for student " + idCard + " in course " + courseCode + " for year " + year);
        }
        
        // Get pending scores (scores with NULL values)
        List<Score> pendingScores = scoreDAO.findPendingScoresByEnrollmentId(enrollment.getCode());
        
        if (pendingScores.isEmpty()) {
            return new ServiceResult(true, 
                "All subjects for this enrollment already have scores assigned");
        }
        
        logger.info("Found {} pending scores for enrollment {}", pendingScores.size(), enrollment.getCode());
        
        // Interactive score entry
        Scanner scanner = new Scanner(System.in);
        int updatedCount = 0;
        int skippedCount = 0;
        
        System.out.println("\nEntering scores for student: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Course: " + enrollment.getCourse().getName() + " (" + year + ")");
        System.out.println("Enter score (0-10) or 99 to skip:\n");
        
        for (Score score : pendingScores) {
            System.out.print(score.getSubject().getName() + ": ");
            
            try {
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("  Skipped (empty input)");
                    skippedCount++;
                    continue;
                }
                
                Integer scoreValue = Integer.parseInt(input);
                
                if (scoreValue == 99) {
                    System.out.println("  Skipped");
                    skippedCount++;
                    continue;
                }
                
                if (scoreValue < 0 || scoreValue > 10) {
                    System.out.println("  Error: Score must be between 0 and 10. Skipped.");
                    skippedCount++;
                    continue;
                }
                
                score.setScore(scoreValue);
                if (scoreDAO.update(score)) {
                    System.out.println("  Score saved: " + scoreValue);
                    updatedCount++;
                } else {
                    System.out.println("  Error saving score");
                    skippedCount++;
                }
                
            } catch (NumberFormatException e) {
                System.out.println("  Error: Invalid number format. Skipped.");
                skippedCount++;
            }
        }
        
        String message = String.format("\nQualification completed: %d scores updated, %d skipped", 
            updatedCount, skippedCount);
        logger.info(message);
        
        return new ServiceResult(true, message);
    }
    
    /**
     * Prints the student's transcript for a specific enrollment
     * @param idCard Student ID card
     * @param courseCode Course code
     * @param year Enrollment year
     * @return ServiceResult with success status and message
     */
    public ServiceResult printTranscript(String idCard, Integer courseCode, Integer year) {
        // Validate student exists
        Student student = studentDAO.findByIdCard(idCard).orElse(null);
        if (student == null) {
            return new ServiceResult(false, "Error: Student with ID card '" + idCard + "' not found");
        }
        
        // Find enrollment
        Enrollment enrollment = enrollmentDAO.findByStudentCourseYear(idCard, courseCode, year).orElse(null);
        if (enrollment == null) {
            return new ServiceResult(false, 
                "Error: No enrollment found for student " + idCard + " in course " + courseCode + " for year " + year);
        }
        
        // Get all scores for this enrollment
        List<Score> scores = scoreDAO.findByEnrollmentId(enrollment.getCode());
        
        if (scores.isEmpty()) {
            return new ServiceResult(false, 
                "No subjects found for this enrollment");
        }
        
        // Print transcript
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TRANSCRIPT - VOCATIONAL TRAINING SCHOOL");
        System.out.println("=".repeat(70));
        System.out.println("Student: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("ID Card: " + student.getIdCard());
        System.out.println("Course: " + enrollment.getCourse().getName());
        System.out.println("Year: " + year);
        System.out.println("=".repeat(70));
        System.out.println();
        System.out.printf("%-40s %10s %10s%n", "SUBJECT", "YEAR", "SCORE");
        System.out.println("-".repeat(70));
        
        int totalSubjects = 0;
        int passedSubjects = 0;
        double totalScore = 0;
        int gradedSubjects = 0;
        
        for (Score score : scores) {
            String subjectName = score.getSubject().getName();
            Integer subjectYear = score.getSubject().getYear();
            Integer scoreValue = score.getScore();
            String scoreStr = (scoreValue != null) ? scoreValue.toString() : "PENDING";
            
            System.out.printf("%-40s %10d %10s%n", subjectName, subjectYear, scoreStr);
            
            totalSubjects++;
            if (scoreValue != null) {
                gradedSubjects++;
                totalScore += scoreValue;
                if (scoreValue >= 5) {
                    passedSubjects++;
                }
            }
        }
        
        System.out.println("-".repeat(70));
        System.out.println("\nSUMMARY:");
        System.out.println("Total subjects: " + totalSubjects);
        System.out.println("Graded subjects: " + gradedSubjects);
        System.out.println("Pending subjects: " + (totalSubjects - gradedSubjects));
        System.out.println("Passed subjects: " + passedSubjects);
        System.out.println("Failed subjects: " + (gradedSubjects - passedSubjects));
        
        if (gradedSubjects > 0) {
            double average = totalScore / gradedSubjects;
            System.out.printf("Average score: %.2f%n", average);
        }
        
        System.out.println("=".repeat(70));
        System.out.println();
        
        logger.info("Transcript printed for student {} in course {} for year {}", idCard, courseCode, year);
        return new ServiceResult(true, "Transcript printed successfully");
    }
}
