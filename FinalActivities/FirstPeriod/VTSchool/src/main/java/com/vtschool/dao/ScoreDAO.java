package com.vtschool.dao;

import com.vtschool.model.Score;
import com.vtschool.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ScoreDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(ScoreDAO.class);
    
    /**
     * Saves a score to the database
     * @param score The score to save
     * @return true if saved successfully, false otherwise
     */
    public boolean save(Score score) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(score);
            transaction.commit();
            logger.info("Score saved: {}", score.getCode());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving score: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Saves a list of scores in a single transaction
     * @param scores List of scores to save
     * @return true if all saved successfully, false otherwise
     */
    public boolean saveAll(List<Score> scores) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            for (Score score : scores) {
                session.persist(score);
            }
            
            transaction.commit();
            logger.info("Successfully saved {} scores", scores.size());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving scores: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all scores for a specific enrollment
     * @param enrollmentId The enrollment ID
     * @return List of scores
     */
    public List<Score> findByEnrollmentId(Integer enrollmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Score s " +
                        "LEFT JOIN FETCH s.subject " +
                        "WHERE s.enrollment.code = :enrollmentId";
            Query<Score> query = session.createQuery(hql, Score.class);
            query.setParameter("enrollmentId", enrollmentId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding scores by enrollment: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Updates a score
     * @param score The score to update
     * @return true if updated successfully, false otherwise
     */
    public boolean update(Score score) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(score);
            transaction.commit();
            logger.info("Score updated: {}", score.getCode());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating score: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all scores with NULL values for a specific enrollment
     * @param enrollmentId The enrollment ID
     * @return List of scores with NULL values
     */
    public List<Score> findPendingScoresByEnrollmentId(Integer enrollmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Score s " +
                        "LEFT JOIN FETCH s.subject " +
                        "WHERE s.enrollment.code = :enrollmentId AND s.score IS NULL";
            Query<Score> query = session.createQuery(hql, Score.class);
            query.setParameter("enrollmentId", enrollmentId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding pending scores by enrollment: {}", e.getMessage());
            return List.of();
        }
    }
}
