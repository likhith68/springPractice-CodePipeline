package com.codingshuttle.TestingApp.controllers;

import com.codingshuttle.TestingApp.dto.EmployeeDto;
import com.codingshuttle.TestingApp.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout= "100000")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AbstractIntegrationTest {

    Employee testEmployee = Employee.builder()
            .name("Test Employee")
            .email("test@gmail.com")
            .salary(10000L)
            .build();

    EmployeeDto testEmployeeDto = EmployeeDto.builder()
            .name("Test Employee")
            .email("test@gmail.com")
            .salary(10000L)
            .build();

    @Autowired
    WebTestClient webTestClient;
}
