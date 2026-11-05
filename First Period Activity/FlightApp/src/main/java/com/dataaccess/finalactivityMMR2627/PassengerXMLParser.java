package com.dataaccess.finalactivityMMR2627;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PassengerXMLParser {

    // Método principal que lee el archivo XML y devuelve una lista de pasajeros válidos
    public static List<Passenger> parsePassengers(String filePath) throws Exception {
        File xmlFile = new File(filePath);
        if (!xmlFile.exists()) {
            throw new Exception("File not found: " + filePath);
        }

        // 1. Configuramos el lector de XML de Java
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // 2. Buscamos todas las etiquetas <passenger> en el documento
        NodeList nodeList = doc.getElementsByTagName("passenger");
        List<Passenger> passengers = new ArrayList<>();

        // 3. Recorremos cada etiqueta <passenger> encontrada
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                // Extraemos el texto de cada etiqueta hija
                String passportNo = getTagValue("passportno", element);
                String firstName = getTagValue("firstname", element);
                String lastName = getTagValue("lastname", element);
                String address = getTagValue("address", element);
                String phone = getTagValue("phone", element);

                // --- VALIDACIONES SEGÚN EL PDF ---

                // A) Campos obligatorios: passportno, firstname, lastname
                if (passportNo == null || passportNo.trim().isEmpty()) {
                    throw new Exception("Validation error in passenger #" + (i + 1) + ": passportno is mandatory.");
                }
                if (firstName == null || firstName.trim().isEmpty()) {
                    throw new Exception("Validation error in passenger #" + (i + 1) + ": firstname is mandatory.");
                }
                if (lastName == null || lastName.trim().isEmpty()) {
                    throw new Exception("Validation error in passenger #" + (i + 1) + ": lastname is mandatory.");
                }

                // B) Validación de formato de Pasaporte (ejemplo: 1 letra mayúscula y 7 u 8 números)
                if (!passportNo.matches("^[A-Za-z0-9]{7,10}$")) {
                    throw new Exception("Validation error: Invalid passport format for " + passportNo);
                }

                // C) Validación de formato de Teléfono (si viene relleno, debe contener solo números, entre 7 y 15 dígitos)
                if (phone != null && !phone.trim().isEmpty()) {
                    if (!phone.matches("^[0-9+ ]{7,15}$")) {
                        throw new Exception("Validation error: Invalid phone format for passenger " + passportNo + ": " + phone);
                    }
                }

                // Si todo está correcto, creamos el objeto Passenger y lo añadimos a la lista
                Passenger p = new Passenger(passportNo.trim(), firstName.trim(), lastName.trim(), address, phone);
                passengers.add(p);
            }
        }

        return passengers;
    }

    // Método auxiliar para obtener el texto dentro de una etiqueta XML de forma segura
    private static String getTagValue(String tag, Element element) {
        NodeList nl = element.getElementsByTagName(tag);
        if (nl != null && nl.getLength() > 0) {
            Node node = nl.item(0);
            if (node != null && node.getFirstChild() != null) {
                return node.getFirstChild().getNodeValue();
            }
        }
        return null;
    }
}