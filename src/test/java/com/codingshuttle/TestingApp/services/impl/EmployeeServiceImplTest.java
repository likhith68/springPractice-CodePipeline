package com.codingshuttle.TestingApp.services.impl;

import com.codingshuttle.TestingApp.dto.EmployeeDto;
import com.codingshuttle.TestingApp.entities.Employee;
import com.codingshuttle.TestingApp.exceptions.ResourceNotFoundException;
import com.codingshuttle.TestingApp.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class EmployeeServiceImplTest {

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setup(){
        log.info("Setting up Mock Configs");
        mockEmployee = Employee.builder()
                .id(1L)
                .name("Mocked Employee 1")
                .email("mockEmp1@gmail.com")
                .salary(1000L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee,EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_whenEmployeeIsPresent_thenReturnEmployeeDto(){

//        given
//    we are testing employeeservice here and not the employeeRepository, so employeeRepository calls will be mocked
        Long id = mockEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));


//        when
        EmployeeDto employeeById = employeeServiceImpl.getEmployeeById(id);
        EmployeeDto employeeById2 = employeeServiceImpl.getEmployeeById(id);

//        then
        Assertions.assertThat(employeeById.getId()).isEqualTo(id);
        Assertions.assertThat(employeeById.getEmail()).isEqualTo(mockEmployee.getEmail());

        verify(employeeRepository,times(2)).findById(id);
    }

    @Test
    void testGetEmployeeById_whenEmployeeIsNotPresent_thenThrowException(){
        //given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when and then assert
        Assertions.assertThatThrownBy(
                ()->employeeServiceImpl.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
    }

    @Test
    void testCreateNewEmployee_whenEmployeeIsValid_thenSaveEmployee(){
//        given
        when(employeeRepository.findByEmail(mockEmployee.getEmail())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

//        when
        EmployeeDto savedEmp = employeeServiceImpl.createNewEmployee(mockEmployeeDto);

//        then
        Assertions.assertThat(savedEmp.getEmail()).isEqualTo(mockEmployee.getEmail());
        Assertions.assertThat(savedEmp.getName()).isEqualTo(mockEmployee.getName());

        verify(employeeRepository,times(1)).findByEmail(mockEmployee.getEmail());

        ArgumentCaptor<Employee> argumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(argumentCaptor.capture());

        Employee capturedEmp = argumentCaptor.getValue();
        Assertions.assertThat(capturedEmp.getEmail()).isEqualTo(mockEmployee.getEmail());

    }

    @Test
    void testCreateNewEmployee_whenEmployeeExists_thenThrowException(){
        //given
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of(new Employee()));

        //when and assert
        Assertions.assertThatThrownBy(
                ()->employeeServiceImpl.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployeeDto.getEmail());

        verify(employeeRepository,times(1)).findByEmail(anyString());
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmployeeExists_thenSaveEmployee(){
        //given
        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee);
        //when
        employeeServiceImpl.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto);

        //then assert
        verify(employeeRepository,times(1)).findById(mockEmployee.getId());

        ArgumentCaptor<Employee> argumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository,times(1)).save(argumentCaptor.capture());

        Employee emp = argumentCaptor.getValue();
        Assertions.assertThat(emp.getName()).isEqualTo(mockEmployee.getName());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        //given
        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThatThrownBy(()->
                employeeServiceImpl.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + mockEmployee.getId());


        //then assert
        verify(employeeRepository,times(1)).findById(mockEmployee.getId());
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_whenEmailIsUpdated_thenThrowException(){
        //given
        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setEmail("UpdatedMockEmail@gmail.com");

        //when
        Assertions.assertThatThrownBy(()->
                        employeeServiceImpl.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");


        //then assert
        verify(employeeRepository,times(1)).findById(mockEmployee.getId());
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee(){
        //given
        when(employeeRepository.existsById(mockEmployee.getId())).thenReturn(true);

        //when
        employeeServiceImpl.deleteEmployee(mockEmployee.getId());

        //then assert
        verify(employeeRepository,times(1)).existsById(mockEmployee.getId());

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(employeeRepository,times(1)).deleteById(argumentCaptor.capture());
        Long id = argumentCaptor.getValue();

        Assertions.assertThat(id).isEqualTo(mockEmployee.getId());
    }

    @Test
    void testDeleteEmployee_whenEmailNotFound_thenThrowException(){
        //given
        when(employeeRepository.existsById(mockEmployee.getId())).thenReturn(false);

        //when
        Assertions.assertThatThrownBy(()->
                        employeeServiceImpl.deleteEmployee(mockEmployee.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + mockEmployee.getId());

        //then assert
        verify(employeeRepository,times(1)).existsById(mockEmployee.getId());
        verify(employeeRepository,never()).deleteById(any());
    }

}