package com.vtschool.dao;

import com.vtschool.model.Subject;
import com.vtschool.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class SubjectDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(SubjectDAO.class);
    
    /**
     * Finds a subject by its code
     * @param code The subject code
     * @return Optional containing the subject if found
     */
    public Optional<Subject> findByCode(Integer code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Subject subject = session.find(Subject.class, code);
            return Optional.ofNullable(subject);
        } catch (Exception e) {
            logger.error("Error finding subject by code: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Retrieves all subjects from the database
     * @return List of all subjects
     */
    public List<Subject> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Subject> query = session.createQuery("FROM Subject ORDER BY year, code", Subject.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error retrieving all subjects: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Finds subjects by year
     * @param year The year (1 or 2)
     * @return List of subjects for that year
     */
    public List<Subject> findByYear(Integer year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Subject WHERE year = :year ORDER BY code";
            Query<Subject> query = session.createQuery(hql, Subject.class);
            query.setParameter("year", year);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding subjects by year: {}", e.getMessage());
            return List.of();
        }
    }
}
