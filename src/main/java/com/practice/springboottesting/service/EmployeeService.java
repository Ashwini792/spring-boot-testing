package com.practice.springboottesting.service;

import com.practice.springboottesting.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> findAllEmployees();

    Optional<Employee> findByEmployeeId(Long empID);

    Employee updateEmployee(Employee employee);

    void deleteEmployeeByID(Long empID);
}
