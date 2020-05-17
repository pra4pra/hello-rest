package com.tential.hellorest.service;

import com.tential.hellorest.model.Department;
import com.tential.hellorest.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    static final String DEFAULT_SORT_COLUMN = "departmentId";

    @Autowired
    private DepartmentRepository repository;

    @Override
    public Department createDepartment(Department department) {
        return repository.save(department);
    }

    @Override
    public Department updateDepartment(Department department) {
        return repository.save(department);
    }

    @Override
    public List<Department> getDepartments() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, DEFAULT_SORT_COLUMN));
    }

    @Override
    public Optional<Department> getDepartment(Long departmentId) {
        return repository.findById(departmentId);
    }
}
