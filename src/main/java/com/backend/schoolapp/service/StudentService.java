package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Student;
import com.backend.schoolapp.model.StudentDetailDTO;
import com.backend.schoolapp.model.StudentDTO;
import com.backend.schoolapp.model.Exam;
import com.backend.schoolapp.model.Career;
import com.backend.schoolapp.model.Subject;
import com.backend.schoolapp.repository.StudentRepository;
import com.backend.schoolapp.repository.ExamRepository;
import com.backend.schoolapp.repository.CareerRepository;
import com.backend.schoolapp.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repo;
    private final ExamRepository examRepository;
    private final CareerRepository careerRepository;
    private final SubjectRepository subjectRepository;

    public StudentService(StudentRepository repo, ExamRepository examRepository, 
                         CareerRepository careerRepository, SubjectRepository subjectRepository) {
        this.repo = repo;
        this.examRepository = examRepository;
        this.careerRepository = careerRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Student> getAll() { return repo.findAll(); }
    public Student getById(Long id) { return repo.findById(id).orElse(null); }
    public Student save(Student student) {
        System.out.println("Guardando estudiante: " + student.getFirstName() + " " + student.getLastName());
        
        if (student.getId() == null) {
            if (repo.existsByEmail(student.getEmail())) {
                throw new RuntimeException("Ya existe un estudiante con este email");
            }
            if (repo.existsByEnrollment(student.getEnrollment())) {
                throw new RuntimeException("Ya existe un estudiante con esta matrícula");
            }
        } else {
            Student existing = repo.findByEnrollment(student.getEnrollment()).orElse(null);
            if (existing != null && !existing.getId().equals(student.getId())) {
                throw new RuntimeException("Ya existe un estudiante con esta matrícula");
            }
        }
        
        return repo.save(student);
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
    
    public Student createStudent(StudentDTO dto) {
        Student student = convertDTOToStudent(dto);
        return save(student);
    }
    
    public Student updateStudent(Long id, StudentDTO dto) {
        Student student = convertDTOToStudent(dto);
        student.setId(id);
        return save(student);
    }
    
    private Student convertDTOToStudent(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setEnrollment(dto.getEnrollment());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        
        if (dto.getCareerId() != null) {
            Career career = careerRepository.findById(dto.getCareerId()).orElse(null);
            student.setCareer(career);
        }
        
        if (dto.getSubjectIds() != null && !dto.getSubjectIds().isEmpty()) {
            List<Subject> subjects = subjectRepository.findAllById(dto.getSubjectIds());
            student.setSubjects(subjects);
        }
        
        return student;
    }
}
