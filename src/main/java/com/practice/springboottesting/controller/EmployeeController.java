package com.practice.springboottesting.controller;

import com.practice.springboottesting.domain.Employee;
import com.practice.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee)
    {
return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees()
    {
        return employeeService.findAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") long empID)
    {
        return employeeService.findByEmployeeId(empID)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long empId, @RequestBody Employee employee)
    {
        return employeeService.findByEmployeeId(empId)
                .map(savedEmployee->
                {
                    savedEmployee.setFirstName(employee.getFirstName());
                    savedEmployee.setLastName(employee.getLastName());
                    savedEmployee.setEmail(employee.getEmail());
                    employeeService.updateEmployee(savedEmployee);
                            return new ResponseEntity<>(savedEmployee,HttpStatus.OK);
                })
                .orElseGet(()->ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId)
    {
        employeeService.deleteEmployeeByID(employeeId);
        return new ResponseEntity<>("Employee deleted successfully!.", HttpStatus.OK);
    }
}
