package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Teacher;
import com.backend.schoolapp.model.TeacherDTO;
import com.backend.schoolapp.model.Department;
import com.backend.schoolapp.repository.TeacherRepository;
import com.backend.schoolapp.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository repo;
    private final DepartmentRepository departmentRepository;

    public TeacherService(TeacherRepository repo, DepartmentRepository departmentRepository) {
        this.repo = repo;
        this.departmentRepository = departmentRepository;
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
    
    public Teacher createTeacher(TeacherDTO dto) {
        Teacher teacher = convertDTOToTeacher(dto);
        return save(teacher);
    }
    
    public Teacher updateTeacher(Long id, TeacherDTO dto) {
        Teacher teacher = convertDTOToTeacher(dto);
        teacher.setId(id);
        return save(teacher);
    }
    
    private Teacher convertDTOToTeacher(TeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setEnrollment(dto.getEnrollment());
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setEmail(dto.getEmail());
        
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
            teacher.setDepartment(department);
        }
        
        return teacher;
    }
    public void delete(Long id) { repo.deleteById(id); }
}