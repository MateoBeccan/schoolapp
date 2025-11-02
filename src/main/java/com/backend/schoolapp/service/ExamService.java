package com.backend.schoolapp.service;

import com.backend.schoolapp.model.Exam;
import com.backend.schoolapp.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    public List<Exam> getAll() {
        return examRepository.findAll();
    }

    public Exam getById(Long id) {
        return examRepository.findById(id).orElse(null);
    }

    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

    public void delete(Long id) {
        examRepository.deleteById(id);
    }

    public List<Exam> getByStudentId(Long studentId) {
        return examRepository.findByStudentId(studentId);
    }

    public List<Exam> getBySubjectId(Long subjectId) {
        return examRepository.findBySubjectId(subjectId);
    }
}