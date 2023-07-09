package com.practice.springboottesting.service;

import com.practice.springboottesting.domain.Employee;
import com.practice.springboottesting.exception.ResourceNotFoundException;
import com.practice.springboottesting.repository.EmployeeRepository;
import com.practice.springboottesting.service.imple.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee=Employee.builder()
                .firstName("Ashu")
                .lastName("A")
                .email("ashu@gmail.com").build();
    }

    @Test
     void givenEmployee_whenSave_returnSavedEmployee()
    {
        //given
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        //when action perfprmed
        Employee savedEmployee=employeeService.saveEmployee(employee);
        //verify
        Assertions.assertThat(savedEmployee).isNotNull();

    }

    @Test
     void givenExistingEmail_whenSave_throwsException() {
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,()->
        {
            employeeService.saveEmployee(employee);
        });
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
     void givelEmployeeList_whenFindAll_thenReturnListOfEmployees()
    {
        //given
        Employee employee1=Employee.builder()
                .firstName("Arpi")
                .lastName("AB")
                .email("arpitha@gmail.com").build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));
        //when
        List<Employee> empList=employeeService.findAllEmployees();
        //then
        Assertions.assertThat(empList).isNotNull();
        Assertions.assertThat(empList).size().isEqualTo(2);

    }

    @Test
     void givenEmptyList_whenFindAll_thenReturnEmptyList()
    {
        //given
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Employee> empList=employeeService.findAllEmployees();
        //then
        Assertions.assertThat(empList).isEmpty();
        Assertions.assertThat(empList).size().isEqualTo(0);

    }

    @Test
     void givenEmployeeId_findByEmpId_thenReturnEmployee()
    {
        //given
        Long empId=1l;
        BDDMockito.given(employeeRepository.findById(empId)).willReturn(Optional.of(employee));

        //when
        Employee employee=employeeService.findByEmployeeId(empId).get();
        Assertions.assertThat(employee).isNotNull();

    }

    @Test
     void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee()
    {
        //given
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("change@gmail.com");
        employee.setFirstName("Change");
        //when
        Employee updatedEmployee=employeeService.updateEmployee(employee);
        //then
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("change@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Change");
    }

    @Test
     void givenEmployeeId_whenDeleteByEmployeeID_thenReturnNothing()
    {
        //given
        Long empId=1l;
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(empId);

        //when
        employeeService.deleteEmployeeByID(empId);

        //then
        verify(employeeRepository, times(1)).deleteById(empId);
    }

}
