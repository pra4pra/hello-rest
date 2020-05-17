package com.tential.hellorest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tential.hellorest.model.Department;
import com.tential.hellorest.service.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DepartmentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private DepartmentServiceImpl service;

    @BeforeEach
    void init() {

        when(service.createDepartment(getFinanceDepartment())).thenReturn(getFinanceDepartment());
        when(service.getDepartment(1L)).thenReturn(Optional.of(getHrDepartment()));
        when(service.updateDepartment(getHrDepartment())).thenReturn(getHrDepartment());
        when(service.getDepartments()).thenReturn(Arrays.asList(getHrDepartment(), getFinanceDepartment()));
    }

    @Test
    void whenCreateDepartment_withValidUser_thenOK() {

        ResponseEntity<Department> response = restTemplate
                .withBasicAuth("ppaluru", "test1234")
                .postForEntity(API_V_1_DEPARTMENT, getFinanceDepartment(), Department.class);
        printJSON(response);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(getFinanceDepartment().toString(), Objects.requireNonNull(response.getBody()).toString());
    }

    @Test
     void whenCreateDepartment_withNoUser_thenBad() {

        ResponseEntity<Department> response = restTemplate
                .postForEntity(API_V_1_DEPARTMENT, getHrDepartment(), Department.class);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenPutDepartment_withUser_thenOK() {
        ResponseEntity<Department> response = restTemplate.withBasicAuth("ppaluru", "test1234")
                .exchange(API_V_1_DEPARTMENT_1, HttpMethod.PUT, getHREntity(), Department.class);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(getHrDepartment().toString(), Objects.requireNonNull(response.getBody()).toString());
    }

    @Test
    void whenPutDepartment_withNoUser_thenBad() {
        ResponseEntity<Department> response = restTemplate
                .exchange(API_V_1_DEPARTMENT_1, HttpMethod.PUT, getHREntity(), Department.class);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void whenGetDepartment_withFinance_thenOK() {
        ResponseEntity<Department> response = restTemplate.getForEntity(API_V_1_DEPARTMENT_1, Department.class);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getHrDepartment().toString(), Objects.requireNonNull(response.getBody()).toString());
    }

    @Test
    void whenGetDepartment_withNonExists_thenBad() {
        ResponseEntity<Department> response = restTemplate.getForEntity("/api/v1/department/1342", Department.class);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenGetDepartments_withNonExists_thenBad() {
        ResponseEntity<Department> response = restTemplate.getForEntity("/api/v1/sub/departments", Department.class);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void whenGetInvalid_withNonExists_thenBad() {
        ResponseEntity<Department> response = restTemplate.getForEntity("/api/v2/department", Department.class);
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private static HttpEntity getHREntity(){
        String requestBody = "{\"departmentId\":1,\"departmentName\":\"HR Department\",\"employeeCount\":5}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestBody, headers);
    }

    static final String API_V_1_DEPARTMENT_1 = "/api/v1/department/1";
    static final String API_V_1_DEPARTMENT = "/api/v1/department";
    static final String API_V_1_DEPARTMENTS = "/api/v1/departments";

    private static Department getFinanceDepartment(){
        return getDepartment(2L, "Finance Department", 10);
    }

    private static Department getHrDepartment(){
        return getDepartment(1L, "HR Department", 5);
    }

    private static Date getDate(){
        try {
            return new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss SSS").parse("2020-May-16 21:13:20 178");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Department getDepartment(Long departmentId, String departmentName, Integer employeeCount) {
        Department department = new Department();
        department.setDepartmentId(departmentId);
        department.setDepartmentName(departmentName);
        department.setEmployeeCount(employeeCount);
        department.setCreatedAt(getDate());
        department.setUpdatedAt(getDate());
        return department;
    }

    private static final ObjectMapper om = new ObjectMapper();

    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
