package com.codingshuttle.TestingApp.controllers;

import com.codingshuttle.TestingApp.entities.Employee;
import com.codingshuttle.TestingApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmployeeControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_positive(){

        Employee savedEmp = employeeRepository.save(testEmployee);

        webTestClient.get()
                .uri("/employees/{id}",savedEmp.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(savedEmp.getEmail());
    }

    @Test
    void testGetEmployeeById_failure(){

        webTestClient.get()
                .uri("/employees/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeExists_thenThrowException(){

        Employee savedEmp = employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployee)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenNewEmployee_thenCreateEmployee(){

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployee)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployee.getEmail());
    }



}