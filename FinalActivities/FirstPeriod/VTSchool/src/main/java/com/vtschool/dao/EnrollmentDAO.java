package com.vtschool.dao;

import com.vtschool.model.Enrollment;
import com.vtschool.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class EnrollmentDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentDAO.class);
    
    /**
     * Saves an enrollment to the database
     * @param enrollment The enrollment to save
     * @return true if saved successfully, false otherwise
     */
    public boolean save(Enrollment enrollment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(enrollment);
            transaction.commit();
            logger.info("Enrollment saved: {}", enrollment.getCode());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving enrollment: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Finds an enrollment by code
     * @param code The enrollment code
     * @return Optional containing the enrollment if found
     */
    public Optional<Enrollment> findByCode(Integer code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Enrollment enrollment = session.find(Enrollment.class, code);
            return Optional.ofNullable(enrollment);
        } catch (Exception e) {
            logger.error("Error finding enrollment by code: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Finds an enrollment by student ID, course code and year
     * @param studentIdCard Student ID card
     * @param courseCode Course code
     * @param year Year
     * @return Optional containing the enrollment if found
     */
    public Optional<Enrollment> findByStudentCourseYear(String studentIdCard, Integer courseCode, Integer year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Enrollment e " +
                        "LEFT JOIN FETCH e.student " +
                        "LEFT JOIN FETCH e.course " +
                        "WHERE e.student.idCard = :idCard " +
                        "AND e.course.code = :courseCode AND e.year = :year";
            
            Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
            query.setParameter("idCard", studentIdCard);
            query.setParameter("courseCode", courseCode);
            query.setParameter("year", year);
            
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.error("Error finding enrollment: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Gets all enrollments for a student in a specific course
     * @param studentIdCard Student ID card
     * @param courseCode Course code
     * @return List of enrollments
     */
    public List<Enrollment> findByStudentAndCourse(String studentIdCard, Integer courseCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Enrollment e WHERE e.student.idCard = :idCard " +
                        "AND e.course.code = :courseCode ORDER BY e.year";
            
            Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
            query.setParameter("idCard", studentIdCard);
            query.setParameter("courseCode", courseCode);
            
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding enrollments by student and course: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Checks if a student has already completed a course (passed all subjects)
     * @param studentIdCard Student ID card
     * @param courseCode Course code
     * @return true if completed, false otherwise
     */
    public boolean hasCompletedCourse(String studentIdCard, Integer courseCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Get total subjects in the course
            String hqlTotal = "SELECT COUNT(DISTINCT s.code) FROM Subject s " +
                            "JOIN s.courses c WHERE c.code = :courseCode";
            Query<Long> totalQuery = session.createQuery(hqlTotal, Long.class);
            totalQuery.setParameter("courseCode", courseCode);
            Long totalSubjects = totalQuery.getSingleResult();
            
            // Get passed subjects for this student in this course
            String hqlPassed = "SELECT COUNT(DISTINCT sc.subject.code) FROM Score sc " +
                             "JOIN sc.enrollment e " +
                             "WHERE e.student.idCard = :idCard " +
                             "AND e.course.code = :courseCode " +
                             "AND sc.score >= 5";
            Query<Long> passedQuery = session.createQuery(hqlPassed, Long.class);
            passedQuery.setParameter("idCard", studentIdCard);
            passedQuery.setParameter("courseCode", courseCode);
            Long passedSubjects = passedQuery.getSingleResult();
            
            return passedSubjects >= totalSubjects;
        } catch (Exception e) {
            logger.error("Error checking if course is completed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets failed subjects for a student in a specific course
     * Uses stored function to get pending subjects
     * @param studentIdCard Student ID card
     * @param courseCode Course code
     * @return List of subject codes that are pending
     */
    public List<Integer> getPendingSubjects(String studentIdCard, Integer courseCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Get all subjects that the student has NOT passed yet
            String hql = "SELECT DISTINCT s.code FROM Subject s " +
                        "JOIN s.courses c " +
                        "WHERE c.code = :courseCode " +
                        "AND s.code NOT IN (" +
                        "  SELECT sc.subject.code FROM Score sc " +
                        "  JOIN sc.enrollment e " +
                        "  WHERE e.student.idCard = :idCard " +
                        "  AND e.course.code = :courseCode " +
                        "  AND sc.score >= 5" +
                        ")";
            
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("courseCode", courseCode);
            query.setParameter("idCard", studentIdCard);
            
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting pending subjects: {}", e.getMessage());
            return List.of();
        }
    }
}
