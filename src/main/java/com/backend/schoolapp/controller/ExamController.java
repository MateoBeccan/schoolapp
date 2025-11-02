package com.backend.schoolapp.controller;

import com.backend.schoolapp.model.Exam;
import com.backend.schoolapp.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ExamController {
    
    private final ExamService service;

    @GetMapping
    public ResponseEntity<List<Exam>> getAll() {
        try {
            List<Exam> exams = service.getAll();
            return ResponseEntity.ok(exams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getById(@PathVariable Long id) {
        try {
            Exam exam = service.getById(id);
            return exam != null ? ResponseEntity.ok(exam) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Exam>> getByStudentId(@PathVariable Long studentId) {
        try {
            List<Exam> exams = service.getByStudentId(studentId);
            return ResponseEntity.ok(exams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Exam>> getBySubjectId(@PathVariable Long subjectId) {
        try {
            List<Exam> exams = service.getBySubjectId(subjectId);
            return ResponseEntity.ok(exams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Exam> create(@RequestBody Exam exam) {
        try {
            Exam savedExam = service.save(exam);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> update(@PathVariable Long id, @RequestBody Exam exam) {
        try {
            exam.setId(id);
            Exam updatedExam = service.save(exam);
            return ResponseEntity.ok(updatedExam);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}