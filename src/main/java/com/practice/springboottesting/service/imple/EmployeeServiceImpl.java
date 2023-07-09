package com.practice.springboottesting.service.imple;

import com.practice.springboottesting.domain.Employee;
import com.practice.springboottesting.exception.ResourceNotFoundException;
import com.practice.springboottesting.repository.EmployeeRepository;
import com.practice.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

   @Autowired
   private EmployeeRepository employeeRepository;

   // public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
   //     this.employeeRepository=employeeRepository;
   // }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee= employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
       throw  new ResourceNotFoundException("Given email ID already exist: "+employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findByEmployeeId(Long empID) {
        return employeeRepository.findById(empID);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeByID(Long empID) {
 employeeRepository.deleteById(empID);
    }
}
