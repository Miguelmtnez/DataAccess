package com.vtschool.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "scores", schema = "_da_vtschool_2526")
public class Score {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code")
    private Integer code;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", referencedColumnName = "code", nullable = false)
    private Enrollment enrollment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", referencedColumnName = "code", nullable = false)
    private Subject subject;
    
    @Column(name = "score")
    private Integer score;
    
    public Score() {
    }
    
    public Score(Enrollment enrollment, Subject subject, Integer score) {
        this.enrollment = enrollment;
        this.subject = subject;
        this.score = score;
    }
    
    // Getters and Setters
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public Enrollment getEnrollment() {
        return enrollment;
    }
    
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }
    
    public Subject getSubject() {
        return subject;
    }
    
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score1 = (Score) o;
        return Objects.equals(code, score1.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return "Score{" +
                "code=" + code +
                ", score=" + score +
                '}';
    }
}
