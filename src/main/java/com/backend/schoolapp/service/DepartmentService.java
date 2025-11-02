package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Department;
import com.backend.schoolapp.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository repo;

    public DepartmentService(DepartmentRepository repo) {
        this.repo = repo;
    }

    public List<Department> getAll() { return repo.findAll(); }
    public Department getById(Long id) { return repo.findById(id).orElse(null); }
    public Department save(Department department) { return repo.save(department); }
    public void delete(Long id) { repo.deleteById(id); }
}