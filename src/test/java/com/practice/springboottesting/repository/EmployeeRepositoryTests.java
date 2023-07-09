package com.practice.springboottesting.repository;

import com.practice.springboottesting.domain.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    EmployeeRepository employeeRepository;
    private Employee emp;

    @BeforeEach
    public void setUp()
    {
         emp= Employee.builder()
                .firstName("Ashu")
                .lastName("A")
                .email("ashu@gmail.com").build();
    }

   @Test
   public void givenEmployeeObject_whenSave_thenReturnSavedEmployee()
    {

        Employee savedEmployee=employeeRepository.save(emp);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    public void givenEmployeeList_whenFindAll_thenReturnEmployeeList()
    {
        //given data, setup
        Employee emp1= Employee.builder()
                .firstName("Arpitha")
                .lastName("R")
                .email("arpitha@gmail.com").build();
        employeeRepository.save(emp);
        employeeRepository.save(emp1);

        // then perform action
        List<Employee> result=employeeRepository.findAll();
        //then verify
        assertThat(result).isNotNull();
        assertThat(result).size().isEqualTo(2);
    }

     @Test
         public void givenEmployeeObject_whenFindById_thenReturnEmployee()
         {
             //given data, setup
             employeeRepository.save(emp);

             // then perform action
             Employee result = employeeRepository.findById(emp.getId()).get();

             //then verify
             assertThat(result).isNotNull();
         }

         @Test
         public void givenEmployye_whenFindByEmail_thenReturnEmployee()
         {
             employeeRepository.save(emp);
             Employee result=employeeRepository.findByEmail(emp.getEmail()).get();
             assertThat(result).isNotNull();
             assertThat(result.getEmail().equals(emp.getEmail()));

         }

          @Test
              public void givenEmployeeEmail_whenfindbyemail_thenReturnUpdatedEmployee()
              {
                  //given data, setup
                  employeeRepository.save(emp);
                  Employee savedEmployee=employeeRepository.findById(emp.getId()).get();
                  savedEmployee.setFirstName("Arpitha");
                  savedEmployee.setEmail("arpi@gmail.com");


                  // then perform action
                  Employee updatedEmployee=employeeRepository.save(savedEmployee);

                  //then verify
                  assertThat(savedEmployee).isNotNull();
                  assertThat(updatedEmployee.getFirstName()).isEqualTo("Arpitha");
                  assertThat(updatedEmployee.getEmail()).isEqualTo("arpi@gmail.com");
              }

               @Test
                   public void givenEmployee_whenDelete_thenReturn()
                   {
                       //given data, setup
                       employeeRepository.save(emp);

                       // then perform action
                        employeeRepository.delete(emp);
                       Optional<Employee> deletedEmployee=employeeRepository.findById(emp.getId());



                       //then verify
                       assertThat(deletedEmployee).isEmpty();
                   }

                    @Test
                        public void givenFirstNameLastName_whenFindBy_thenReturnEmployee()
                        {
                            //given data, setup
                            employeeRepository.save(emp);

                            // then perform action
                            Employee result=employeeRepository.getEmployeeByfirstNameAndLAstName(emp.getFirstName(),emp.getLastName()).get();

                            //then verify
                            assertThat(result).isNotNull();
                        }
}
