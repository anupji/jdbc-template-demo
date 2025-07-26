package com.example;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Main {
    public static void main(String[] args) {
        // 1. Configure DataSource
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        // 2. Initialize JdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // 3. DAO
        EmployeeDAO employeeDAO = new EmployeeDAO(jdbcTemplate);

        // 4. Operations
        Employee emp = new Employee("John Doe", "HR", 50000);
        employeeDAO.addEmployee(emp);
        System.out.println("Employee added.");

        employeeDAO.findEmployeeById(1).ifPresentOrElse(
                e -> System.out.println("Found: " + e.getName()),
                () -> System.out.println("Employee not found")
        );

        employeeDAO.updateEmployee(new Employee(1, "John Smith", "HR", 55000));
        System.out.println("Employee updated.");

        double sum = employeeDAO.getSalarySum();
        double avg = employeeDAO.getSalaryAvg();
        System.out.println("Salary Sum: " + sum);
        System.out.println("Salary Avg: " + avg);

        employeeDAO.deleteEmployee(1);
        System.out.println("Employee deleted.");
    }
}
