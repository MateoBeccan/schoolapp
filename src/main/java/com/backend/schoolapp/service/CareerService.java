package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Career;
import com.backend.schoolapp.repository.CareerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerService {
    private final CareerRepository repo;

    public CareerService(CareerRepository repo) {
        this.repo = repo;
    }

    public List<Career> getAll() { return repo.findAll(); }
    public Career getById(Long id) { return repo.findById(id).orElse(null); }
    public Career save(Career career) { 
        System.out.println("Guardando carrera: " + career.getName());
        Career saved = repo.save(career);
        System.out.println("Carrera guardada con ID: " + saved.getId());
        return saved;
    }
    public void delete(Long id) { repo.deleteById(id); }
}