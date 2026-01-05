package com.codingshuttle.TestingApp.repositories;

import com.codingshuttle.TestingApp.entities.Employee;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@DataJpaTest
@Slf4j
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setup(){
        employee = Employee.builder()
                .email("Likhithjsc@gmail.com")
                .name("Likhith JSC")
                .salary(10000000L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployeeList() {
        //Given
        employeeRepository.save(employee);

        //When
        List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail());

        //Then
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList).isNotEmpty();
        Assertions.assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
        log.info("testFindByEmail_whenEmailIsPresent_thenReturnEmployeeList - PASSED");
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList() {
        //Given
        String email = "test@gmail.com";

        //When
        List<Employee> employeeList = employeeRepository.findByEmail(email);

        //Then
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
        log.info("testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList - PASSED");
    }

}