package com.practice.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springboottesting.domain.Employee;
import com.practice.springboottesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
 class EmployeeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    private Employee employee;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
     void setup()
    {
        employee=Employee.builder().firstName("Ashwini")
                .lastName("As")
                .email("ashu@gmail.com")
                .build();
    }

    @Test
     void givenEmployee_whenCreateEmployee_thenReturnEmployee() throws Exception
    {
        //given
        BDDMockito.given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation)->invocation.getArgument(0));
        //when
       ResultActions response= mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //verfy
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())));



    }

    @Test
     void givenListOfEmployees_whenFindAllEmployees_thenReturnEmployeeList() throws Exception
    {
//given
        Employee emp1= Employee.builder().firstName("Arpi").lastName("Ak").email("Arpi@gmail.com").build();
        List<Employee> empList= List.of(employee,emp1);
        BDDMockito.given(employeeService.findAllEmployees()).willReturn(empList);

        //when
        ResultActions result=mockMvc.perform(get("/api/employee"));
        //verify
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",CoreMatchers.is(empList.size())));


    }
    @Test
     void ginEmployeeID_getEmployeeByEmployeeId_thenReturnEmployee() throws Exception
    {
        //given
        long employeeId=1l;
        BDDMockito.given(employeeService.findByEmployeeId(employeeId)).willReturn(Optional.of(employee));
        //when
        ResultActions result=mockMvc.perform(get("/api/employee/{id}",employeeId));
        //verify
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())));
    }

    @Test
     void givenUpdatedEmployee_WhenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception
    {
        long empId=1l;
        Employee updateEmployee=Employee.builder().firstName("Arpi").lastName("XYZ").email("arpi@gmail.com").build();
        //given
        BDDMockito.given(employeeService.findByEmployeeId(empId)).willReturn(Optional.of(employee));

        BDDMockito.given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation)->invocation.getArgument(0));
        //when
        ResultActions resultActions=mockMvc.perform(put("/api/employee/{id}",empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(updateEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(updateEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(updateEmployee.getEmail())));
    }

    @Test
     void givenUpdateEmployee_whenFailedToUpdate_thenReturnNotFound() throws Exception
    {
        long empId=1l;
        Employee updateEmployee=Employee.builder().firstName("Arpi").lastName("XYZ").email("arpi@gmail.com").build();
        //given
        BDDMockito.given(employeeService.findByEmployeeId(empId)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation)->invocation.getArgument(0));
        //when
        ResultActions resultActions=mockMvc.perform(put("/api/employee/{id}",empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
     void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() throws Exception
    {
        //given
        long empId=1l;
        BDDMockito.willDoNothing().given(employeeService).deleteEmployeeByID(1l);

        //when
        ResultActions result=mockMvc.perform(delete("/api/employee/{id}",empId));
        //verify
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
