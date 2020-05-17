package com.tential.hellorest.service;

import com.tential.hellorest.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department createDepartment(Department department);

    Department updateDepartment(Department department);

    List<Department> getDepartments();

    Optional<Department> getDepartment(Long departmentId);
}
