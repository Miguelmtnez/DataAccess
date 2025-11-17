package com.vtschool.service;

import com.vtschool.dao.*;
import com.vtschool.model.*;
import com.vtschool.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final SubjectDAO subjectDAO;
    
    public EnrollmentService() {
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();
        this.enrollmentDAO = new EnrollmentDAO();
        this.subjectDAO = new SubjectDAO();
    }
    
    /**
     * Enrolls a student in a course for a specific year
     * @param studentIdCard Student ID card
     * @param courseCode Course code
     * @param year Enrollment year
     * @return ServiceResult with success status and message
     */
    public ServiceResult enrollStudent(String studentIdCard, Integer courseCode, Integer year) {
        Transaction transaction = null;
        Session session = null;
        
        try {
            // Validate student exists
            Optional<Student> studentOpt = studentDAO.findByIdCard(studentIdCard);
            if (studentOpt.isEmpty()) {
                return new ServiceResult(false, "Error: Student with ID card " + studentIdCard + " not found");
            }
            Student student = studentOpt.get();
            
            // Validate course exists
            Optional<Course> courseOpt = courseDAO.findByCode(courseCode);
            if (courseOpt.isEmpty()) {
                return new ServiceResult(false, "Error: Course with code " + courseCode + " not found");
            }
            Course course = courseOpt.get();
            
            // Check if student already enrolled in this course for this year
            Optional<Enrollment> existingEnrollment = 
                    enrollmentDAO.findByStudentCourseYear(studentIdCard, courseCode, year);
            if (existingEnrollment.isPresent()) {
                return new ServiceResult(false, 
                        "Error: Student is already enrolled in this course for year " + year);
            }
            
            // Check if student has completed this course
            if (enrollmentDAO.hasCompletedCourse(studentIdCard, courseCode)) {
                return new ServiceResult(false, 
                        "Error: Student has already completed this course and cannot enroll again");
            }
            
            // Get previous enrollments for this student in this course
            List<Enrollment> previousEnrollments = 
                    enrollmentDAO.findByStudentAndCourse(studentIdCard, courseCode);
            
            // Determine which subjects to enroll
            List<Subject> subjectsToEnroll;
            
            if (previousEnrollments.isEmpty()) {
                // First time enrolling in this course - add all year 1 subjects
                subjectsToEnroll = courseDAO.getSubjectsForCourseAndYear(courseCode, 1);
                logger.info("First enrollment for student {} in course {}: {} year 1 subjects", 
                        studentIdCard, courseCode, subjectsToEnroll.size());
            } else {
                // Not first time - add year 2 subjects + failed year 1 subjects
                List<Integer> pendingSubjectCodes = 
                        enrollmentDAO.getPendingSubjects(studentIdCard, courseCode);
                
                // Get year 2 subjects
                List<Subject> year2Subjects = courseDAO.getSubjectsForCourseAndYear(courseCode, 2);
                
                subjectsToEnroll = new ArrayList<>();
                
                // Add year 2 subjects
                subjectsToEnroll.addAll(year2Subjects);
                
                // Add pending year 1 subjects
                for (Integer subjectCode : pendingSubjectCodes) {
                    Optional<Subject> subjectOpt = subjectDAO.findByCode(subjectCode);
                    subjectOpt.ifPresent(subject -> {
                        if (subject.getYear() == 1) {
                            subjectsToEnroll.add(subject);
                        }
                    });
                }
                
                logger.info("Subsequent enrollment for student {} in course {}: {} year 2 subjects + {} pending year 1 subjects",
                        studentIdCard, courseCode, year2Subjects.size(), 
                        subjectsToEnroll.size() - year2Subjects.size());
            }
            
            if (subjectsToEnroll.isEmpty()) {
                return new ServiceResult(false, 
                        "Error: No subjects available to enroll for this student in this course");
            }
            
            // Create enrollment and scores in a single transaction
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Create enrollment
            Enrollment enrollment = new Enrollment(student, course, year);
            session.persist(enrollment);
            session.flush(); // Ensure enrollment ID is generated
            
            // Create scores for all subjects (with null grade)
            List<Score> scores = new ArrayList<>();
            for (Subject subject : subjectsToEnroll) {
                Score score = new Score(enrollment, subject, null);
                session.persist(score);
                scores.add(score);
            }
            
            transaction.commit();
            
            String message = String.format(
                    "Student %s successfully enrolled in course %s (%s) for year %d with %d subjects",
                    studentIdCard, courseCode, course.getName(), year, subjectsToEnroll.size());
            
            logger.info(message);
            return new ServiceResult(true, message);
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error enrolling student: {}", e.getMessage(), e);
            return new ServiceResult(false, "Error: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
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
