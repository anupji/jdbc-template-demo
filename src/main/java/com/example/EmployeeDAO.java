package com.example;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EmployeeDAO {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO Employees (name, department, salary) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, employee.getName(), employee.getDepartment(), employee.getSalary());
    }

    public Optional<Employee> findEmployeeById(int id) {
        String sql = "SELECT * FROM Employees WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new EmployeeMapper())
                .stream().findFirst();
    }

    public void deleteEmployee(int id) {
        String sql = "DELETE FROM Employees WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateEmployee(Employee employee) {
        String sql = "UPDATE Employees SET name = ?, department = ?, salary = ? WHERE id = ?";
        jdbcTemplate.update(sql, employee.getName(), employee.getDepartment(), employee.getSalary(), employee.getId());
    }

    public double getSalarySum() {
        String sql = "SELECT SUM(salary) FROM Employees";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public double getSalaryAvg() {
        String sql = "SELECT AVG(salary) FROM Employees";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    private static class EmployeeMapper implements RowMapper<Employee> {
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Employee(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getDouble("salary")
            );
        }
    }
}
