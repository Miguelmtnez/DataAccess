package com.vtschool.dao;

import com.vtschool.model.Student;
import com.vtschool.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class StudentDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);
    
    /**
     * Saves a student to the database
     * @param student The student to save
     * @return true if saved successfully, false otherwise
     */
    public boolean save(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
            logger.info("Student saved: {}", student.getIdCard());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving student: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Saves a list of students in a single transaction (all or nothing)
     * @param students List of students to save
     * @return true if all students were saved successfully, false otherwise
     */
    public boolean saveAll(List<Student> students) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            for (Student student : students) {
                session.persist(student);
            }
            
            transaction.commit();
            logger.info("Successfully saved {} students", students.size());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving students: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Finds a student by ID card
     * @param idCard The ID card to search for
     * @return Optional containing the student if found
     */
    public Optional<Student> findByIdCard(String idCard) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, idCard);
            return Optional.ofNullable(student);
        } catch (Exception e) {
            logger.error("Error finding student by ID card: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Checks if a student exists by ID card
     * @param idCard The ID card to check
     * @return true if student exists, false otherwise
     */
    public boolean existsByIdCard(String idCard) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(s) FROM Student s WHERE s.idCard = :idCard", Long.class);
            query.setParameter("idCard", idCard);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Error checking if student exists: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves all students from the database
     * @return List of all students
     */
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error retrieving all students: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Updates a student in the database
     * @param student The student to update
     * @return true if updated successfully, false otherwise
     */
    public boolean update(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
            logger.info("Student updated: {}", student.getIdCard());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating student: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a student from the database
     * @param idCard The ID card of the student to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean delete(String idCard) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, idCard);
            if (student != null) {
                session.remove(student);
                transaction.commit();
                logger.info("Student deleted: {}", idCard);
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting student: {}", e.getMessage());
            return false;
        }
    }
}
