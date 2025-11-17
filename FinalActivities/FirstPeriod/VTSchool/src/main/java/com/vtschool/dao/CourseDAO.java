package com.vtschool.dao;

import com.vtschool.model.Course;
import com.vtschool.model.Subject;
import com.vtschool.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CourseDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseDAO.class);
    
    /**
     * Finds a course by its code
     * @param code The course code
     * @return Optional containing the course if found
     */
    public Optional<Course> findByCode(Integer code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.find(Course.class, code);
            return Optional.ofNullable(course);
        } catch (Exception e) {
            logger.error("Error finding course by code: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Retrieves all courses from the database
     * @return List of all courses
     */
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Course> query = session.createQuery("FROM Course", Course.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error retrieving all courses: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Gets all subjects for a specific course and year
     * @param courseCode The course code
     * @param year The year (1 or 2)
     * @return List of subjects
     */
    public List<Subject> getSubjectsForCourseAndYear(Integer courseCode, Integer year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Subject s " +
                        "JOIN s.courses c " +
                        "WHERE c.code = :courseCode AND s.year = :year";
            
            Query<Subject> query = session.createQuery(hql, Subject.class);
            query.setParameter("courseCode", courseCode);
            query.setParameter("year", year);
            
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting subjects for course and year: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Gets all subjects for a specific course
     * @param courseCode The course code
     * @return List of all subjects in the course
     */
    public List<Subject> getAllSubjectsForCourse(Integer courseCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Subject s " +
                        "JOIN s.courses c " +
                        "WHERE c.code = :courseCode " +
                        "ORDER BY s.year, s.code";
            
            Query<Subject> query = session.createQuery(hql, Subject.class);
            query.setParameter("courseCode", courseCode);
            
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all subjects for course: {}", e.getMessage());
            return List.of();
        }
    }
}
