package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Teacher;
import com.backend.schoolapp.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository repo;

    public TeacherService(TeacherRepository repo) {
        this.repo = repo;
    }

    public List<Teacher> getAll() { return repo.findAll(); }
    public Teacher getById(Long id) { return repo.findById(id).orElse(null); }
    public Teacher save(Teacher teacher) { return repo.save(teacher); }
    public void delete(Long id) { repo.deleteById(id); }
}