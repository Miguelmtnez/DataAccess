package com.vtschool.service;

import com.vtschool.model.Student;
import com.vtschool.xml.StudentXML;
import com.vtschool.xml.StudentsWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class XMLParserService {
    
    private static final Logger logger = LoggerFactory.getLogger(XMLParserService.class);
    
    /**
     * Parses an XML file and returns a list of Student entities
     * @param xmlFilePath Path to the XML file
     * @return List of Student entities
     * @throws JAXBException if parsing fails
     */
    public List<Student> parseStudentsFromXML(String xmlFilePath) throws JAXBException {
        File xmlFile = new File(xmlFilePath);
        
        if (!xmlFile.exists()) {
            logger.error("XML file not found: {}", xmlFilePath);
            throw new IllegalArgumentException("File not found: " + xmlFilePath);
        }
        
        JAXBContext jaxbContext = JAXBContext.newInstance(StudentsWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        
        StudentsWrapper wrapper = (StudentsWrapper) unmarshaller.unmarshal(xmlFile);
        
        if (wrapper.getStudents() == null || wrapper.getStudents().isEmpty()) {
            logger.warn("No students found in XML file");
            return List.of();
        }
        
        // Convert StudentXML to Student entities using streams
        return wrapper.getStudents().stream()
                .map(this::convertToStudent)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts StudentXML to Student entity
     * @param studentXML The XML student object
     * @return Student entity
     */
    private Student convertToStudent(StudentXML studentXML) {
        Student student = new Student();
        student.setIdCard(studentXML.getIdCard());
        student.setFirstName(studentXML.getFirstName());
        student.setLastName(studentXML.getLastName());
        
        // Handle optional fields (empty strings are treated as null)
        student.setPhone(studentXML.getPhone() != null && !studentXML.getPhone().isBlank() 
                ? studentXML.getPhone() : null);
        student.setEmail(studentXML.getEmail() != null && !studentXML.getEmail().isBlank() 
                ? studentXML.getEmail() : null);
        
        return student;
    }
}
