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
    public Teacher save(Teacher teacher) {
        System.out.println("Guardando teacher: " + teacher.getFirstName() + " " + teacher.getLastName());
        System.out.println("ID: " + teacher.getId() + ", Matrícula: " + teacher.getEnrollment());
        
        if (teacher.getId() == null) {
            if (repo.existsByEmail(teacher.getEmail())) {
                throw new RuntimeException("Ya existe un profesor con este email");
            }
            if (repo.existsByEnrollment(teacher.getEnrollment())) {
                throw new RuntimeException("Ya existe un profesor con esta matrícula");
            }
        } else {
            // Para actualizaciones, verificar email y matrícula
            Teacher existingByEmail = repo.findById(teacher.getId()).orElse(null);
            if (existingByEmail != null && !existingByEmail.getEmail().equals(teacher.getEmail())) {
                if (repo.existsByEmail(teacher.getEmail())) {
                    throw new RuntimeException("Ya existe un profesor con este email");
                }
            }
            
            Teacher existingByEnrollment = repo.findByEnrollment(teacher.getEnrollment()).orElse(null);
            if (existingByEnrollment != null && !existingByEnrollment.getId().equals(teacher.getId())) {
                throw new RuntimeException("Ya existe un profesor con esta matrícula");
            }
        }
        
        Teacher saved = repo.save(teacher);
        System.out.println("Teacher guardado con ID: " + saved.getId());
        return saved;
    }
    
    public List<Teacher> searchTeachers(String query) {
        return repo.searchByEnrollmentOrName(query);
    }
    public void delete(Long id) { repo.deleteById(id); }
}