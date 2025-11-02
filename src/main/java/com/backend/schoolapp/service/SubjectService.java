package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Subject;
import com.backend.schoolapp.model.SubjectDTO;
import com.backend.schoolapp.model.Teacher;
import com.backend.schoolapp.repository.SubjectRepository;
import com.backend.schoolapp.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository repo;
    private final TeacherRepository teacherRepository;

    public SubjectService(SubjectRepository repo, TeacherRepository teacherRepository) {
        this.repo = repo;
        this.teacherRepository = teacherRepository;
    }

    public List<Subject> getAll() { return repo.findAll(); }
    public Subject getById(Long id) { return repo.findById(id).orElse(null); }
    public Subject save(Subject subject) { return repo.save(subject); }
    
    public Subject createSubject(SubjectDTO dto) {
        Subject subject = convertDTOToSubject(dto);
        return save(subject);
    }
    
    public Subject updateSubject(Long id, SubjectDTO dto) {
        Subject subject = convertDTOToSubject(dto);
        subject.setId(id);
        return save(subject);
    }
    
    private Subject convertDTOToSubject(SubjectDTO dto) {
        Subject subject = new Subject();
        subject.setId(dto.getId());
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        subject.setCredits(dto.getCredits());
        
        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId()).orElse(null);
            subject.setTeacher(teacher);
        }
        
        return subject;
    }
    public void delete(Long id) { repo.deleteById(id); }
}