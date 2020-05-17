package com.tential.hellorest.controller;

import com.tential.hellorest.ResourceNotFoundException;
import com.tential.hellorest.model.Department;
import com.tential.hellorest.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService service;

    //Get to get all Departments
    @GetMapping("/departments")
    List<Department> getAllDepartments() {
        logger.debug("get All Departments start");
        return service.getDepartments();
    }

    // get by department id
    @GetMapping("/department/{departmentId}")
    ResponseEntity<Department> findOne(@PathVariable @Min(1) Long departmentId) {

        logger.debug("Get Department by id {} start", departmentId);
        Department department = service.getDepartment(departmentId).orElseThrow(() ->
                new ResourceNotFoundException("No Department exists with Department Id:{}" + departmentId));
        logger.info("find a department by id:{}", departmentId);
        return ResponseEntity.ok(department);
    }

    // insert/save department.
    @PostMapping("/department")
    ResponseEntity<Department> newDepartment(@Valid @RequestBody Department newDepartment) {
        logger.debug("new Department creation started :{}", newDepartment);
        Department department = service.createDepartment(newDepartment);
        return ResponseEntity.accepted().body(department);
    }

    // Save or update
    @PutMapping("/department/{departmentId}")
    ResponseEntity<Department> updateDepartment(@RequestBody Department newDepartment, @PathVariable Long departmentId) {
        logger.debug("updateDepartment started :{}", newDepartment);
        Department department = service.getDepartment(departmentId).map(
                original -> {
                    original.setDepartmentName(newDepartment.getDepartmentName());
                    original.setEmployeeCount(newDepartment.getEmployeeCount());
                    return service.updateDepartment(original);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("No Department exists with Department Id:{}" + departmentId));

        return ResponseEntity.accepted().body(department);
    }

}
