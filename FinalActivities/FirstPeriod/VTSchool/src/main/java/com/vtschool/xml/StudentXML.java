package com.vtschool.xml;

import jakarta.xml.bind.annotation.*;

import java.sql.Date;

@XmlAccessorType(XmlAccessType.FIELD)
public class StudentXML {
    
    @XmlElement(name = "firstname")
    private String firstName;
    
    @XmlElement(name = "lastname")
    private String lastName;
    
    @XmlElement(name = "idcard")
    private String idCard;
    
    @XmlElement(name = "phone")
    private String phone;
    
    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "birthdate")
    private Date birthdate;
    
    public StudentXML() {
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
    
    public String getIdCard() {
        return idCard;
    }
    
    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "StudentXML{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", birthdate='" + birthdate + '\'' +
                '}';
    }
}
