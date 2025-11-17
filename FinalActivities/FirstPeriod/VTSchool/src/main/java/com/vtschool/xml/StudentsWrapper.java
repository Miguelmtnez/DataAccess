package com.vtschool.xml;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "students")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentsWrapper {
    
    @XmlElement(name = "student")
    private List<StudentXML> students;
    
    public StudentsWrapper() {
    }
    
    public List<StudentXML> getStudents() {
        return students;
    }
    
    public void setStudents(List<StudentXML> students) {
        this.students = students;
    }
}
