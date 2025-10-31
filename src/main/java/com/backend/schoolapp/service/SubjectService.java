package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Subject;
import com.backend.schoolapp.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository repo;

    public SubjectService(SubjectRepository repo) {
        this.repo = repo;
    }

    public List<Subject> getAll() { return repo.findAll(); }
    public Subject getById(Long id) { return repo.findById(id).orElse(null); }
    public Subject save(Subject subject) { return repo.save(subject); }
    public void delete(Long id) { repo.deleteById(id); }
}