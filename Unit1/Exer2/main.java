package Unit1.Exer2;

import java.util.ArrayList;
import java.util.List;

// Base class Person
class Person {
    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void showData() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

// Employee class that inherits from Person
class Employee extends Person {
    protected float grossSalary;

    public Employee(String name, int age, float grossSalary) {
        super(name, age);
        this.grossSalary = grossSalary;
    }

    public float getGrossSalary() {
        return grossSalary;
    }

    @Override
    public void showData() {
        super.showData();
        System.out.println("Gross salary: " + grossSalary);
    }
}

// Executive class that inherits from Employee
class Executive extends Employee {
    private String category;
    private List<Employee> subordinates;

    public Executive(String name, int age, float grossSalary, String category) {
        super(name, age, grossSalary);
        this.category = category;
        this.subordinates = new ArrayList<>();
    }

    public void addSubordinate(Employee employee) {
        subordinates.add(employee);
    }

    public int getSubordinates() {
        return subordinates.size();
    }

    @Override
    public void showData() {
        super.showData();
        System.out.println("Category: " + category + ", Subordinates: " + getSubordinates());
    }
}

// Client class that inherits from Person
class Client extends Person {
    private String phone;

    public Client(String name, int age, String phone) {
        super(name, age);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public void showData() {
        super.showData();
        System.out.println("Phone: " + phone);
    }
}

// Company class
class Company {
    private String name;
    private List<Employee> employees;
    private List<Client> clients;

    public Company(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public int getNumClients() {
        return clients.size();
    }

    public void showCompany() {
        System.out.println("Company: " + name);
        System.out.println("Number of clients: " + getNumClients());

        System.out.println("\n--- Employees ---");
        for (Employee employee : employees) {
            employee.showData();
            System.out.println("-----------------");
        }

        System.out.println("\n--- Clients ---");
        for (Client client : clients) {
            client.showData();
            System.out.println("-----------------");
        }
    }
}

// Main class to test
class CompanyApp {
    public static void main(String[] args) {
        Company company = new Company("TechCorp");

        Employee emp1 = new Employee("Ana", 30, 2000f);
        Employee emp2 = new Employee("Luis", 25, 1500f);
        Executive exec1 = new Executive("Carlos", 45, 5000f, "Manager");

        exec1.addSubordinate(emp1);
        exec1.addSubordinate(emp2);

        Client client1 = new Client("Marta", 40, "123456789");
        Client client2 = new Client("Pepe", 35, "987654321");

        company.addEmployee(emp1);
        company.addEmployee(emp2);
        company.addEmployee(exec1);

        company.addClient(client1);
        company.addClient(client2);

        company.showCompany();
    }
}

