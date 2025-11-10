package com.vtschool.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "students", schema = "_da_vtschool_2526")
public class Student {
    
    @Id
    @Column(name = "idcard", length = 8, nullable = false)
    private String idCard;
    
    @Column(name = "firstname", length = 50, nullable = false)
    private String firstName;
    
    @Column(name = "lastname", length = 100, nullable = false)
    private String lastName;
    
    @Column(name = "phone", length = 12)
    private String phone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();
    
    public Student() {
    }
    
    public Student(String idCard, String firstName, String lastName) {
        this.idCard = idCard;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public Student(String idCard, String firstName, String lastName, String phone, String email) {
        this.idCard = idCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }
    
    // Getters and Setters
    public String getIdCard() {
        return idCard;
    }
    
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(idCard, student.idCard);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idCard);
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "idCard='" + idCard + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
