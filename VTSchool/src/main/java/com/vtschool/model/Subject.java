package com.vtschool.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "subjects", schema = "_da_vtschool_2526")
public class Subject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code")
    private Integer code;
    
    @Column(name = "name", length = 50)
    private String name;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "hours")
    private Integer hours;
    
    @ManyToMany(mappedBy = "subjects")
    private List<Course> courses = new ArrayList<>();
    
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();
    
    public Subject() {
    }
    
    public Subject(String name, Integer year, Integer hours) {
        this.name = name;
        this.year = year;
        this.hours = hours;
    }
    
    // Getters and Setters
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public Integer getHours() {
        return hours;
    }
    
    public void setHours(Integer hours) {
        this.hours = hours;
    }
    
    public List<Course> getCourses() {
        return courses;
    }
    
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    
    public List<Score> getScores() {
        return scores;
    }
    
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(code, subject.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return "Subject{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", hours=" + hours +
                '}';
    }
}
