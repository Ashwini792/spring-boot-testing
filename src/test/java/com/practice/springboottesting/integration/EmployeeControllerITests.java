package com.practice.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springboottesting.domain.Employee;
import com.practice.springboottesting.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class EmployeeControllerITests extends AbstractionBaseTest{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
     void setup()
    {
        employeeRepository.deleteAll();
    }

    @Test
     void givenEmployee_whenSaveEmployee_thenReturnSavedEmployee() throws Exception
    {
        //given
        Employee employee=Employee.builder().firstName("Ashwini")
                .lastName("As")
                .email("ashu@gmail.com")
                .build();
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
        Employee employee=Employee.builder().firstName("Ashwini")
                .lastName("As")
                .email("ashu@gmail.com")
                .build();
        Employee emp1= Employee.builder().firstName("Arpi").lastName("Ak").email("Arpi@gmail.com").build();
        List<Employee> empList= List.of(employee,emp1);
      employeeRepository.saveAll(empList);

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
        //long employeeId=1l;
        Employee employee=Employee.builder().firstName("Ashwini")
                .lastName("As")
                .email("ashu@gmail.com")
                .build();
     employeeRepository.save(employee);
        //when
        ResultActions result=mockMvc.perform(get("/api/employee/{id}",employee.getId()));
        //verify
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(employee.getFirstName())));
    }

    @Test
     void givenUpdatedEmployee_WhenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception
    {
        long empId=1l;
        Employee employee=Employee.builder().firstName("Ashwini")
                .lastName("As")
                .email("ashu@gmail.com")
                .build();
        employeeRepository.save(employee);
        Employee updateEmployee=Employee.builder().firstName("Arpi").lastName("XYZ").email("arpi@gmail.com").build();
        //given

        //when
        ResultActions resultActions=mockMvc.perform(put("/api/employee/{id}",employee.getId())
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
        Employee employee=Employee.builder().firstName("Ashwini")
                .lastName("As")
                .email("ashu@gmail.com")
                .build();
        employeeRepository.save(employee);
        Employee updateEmployee=Employee.builder().firstName("Arpi").lastName("XYZ").email("arpi@gmail.com").build();
        //given

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
        Employee employee=Employee.builder().firstName("Ashwini")
                .lastName("As")
                .email("ashu@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when
        ResultActions result=mockMvc.perform(delete("/api/employee/{id}",employee.getId()));
        //verify
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
