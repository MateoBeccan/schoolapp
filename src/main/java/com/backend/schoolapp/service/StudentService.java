package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Student;
import com.backend.schoolapp.model.StudentDetailDTO;
import com.backend.schoolapp.model.Exam;
import com.backend.schoolapp.repository.StudentRepository;
import com.backend.schoolapp.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repo;
    private final ExamRepository examRepository;

    public StudentService(StudentRepository repo, ExamRepository examRepository) {
        this.repo = repo;
        this.examRepository = examRepository;
    }

    public List<Student> getAll() { return repo.findAll(); }
    public Student getById(Long id) { return repo.findById(id).orElse(null); }
    public Student save(Student student) {
        System.out.println("Guardando estudiante: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Carrera: " + (student.getCareer() != null ? student.getCareer().getName() : "null"));
        
        if (student.getId() == null) {
            if (repo.existsByEmail(student.getEmail())) {
                throw new RuntimeException("Ya existe un estudiante con este email");
            }
            if (repo.existsByEnrollment(student.getEnrollment())) {
                throw new RuntimeException("Ya existe un estudiante con esta matrícula");
            }
        } else {
            // Para actualizaciones, verificar que la matrícula no esté en uso por otro estudiante
            Student existing = repo.findByEnrollment(student.getEnrollment()).orElse(null);
            if (existing != null && !existing.getId().equals(student.getId())) {
                throw new RuntimeException("Ya existe un estudiante con esta matrícula");
            }
        }
        
        Student saved = repo.save(student);
        System.out.println("Estudiante guardado con ID: " + saved.getId());
        return saved;
    }
    public void delete(Long id) { repo.deleteById(id); }
    
    public StudentDetailDTO getStudentDetailByEnrollment(String enrollment) {
        System.out.println("Buscando estudiante con matrícula: " + enrollment);
        
        // Primero buscar el estudiante básico
        Student student = repo.findByEnrollment(enrollment).orElse(null);
        if (student == null) {
            System.out.println("No se encontró estudiante con matrícula: " + enrollment);
            return null;
        }
        
        System.out.println("Estudiante encontrado: " + student.getFirstName() + " " + student.getLastName());
        
        // Buscar exámenes
        List<Exam> exams = examRepository.findByStudentId(student.getId());
        System.out.println("Exámenes encontrados: " + exams.size());
        
        return new StudentDetailDTO(student, exams);
    }
    
    public Student findByEnrollment(String enrollment) {
        return repo.findByEnrollment(enrollment).orElse(null);
    }
}
