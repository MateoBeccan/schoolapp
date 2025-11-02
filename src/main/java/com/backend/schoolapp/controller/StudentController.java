package com.backend.schoolapp.controller;

import com.backend.schoolapp.model.Student;
import com.backend.schoolapp.model.StudentDetailDTO;
import com.backend.schoolapp.model.ErrorResponse;
import com.backend.schoolapp.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        try {
            List<Student> students = service.getAll();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        try {
            Student student = service.getById(id);
            return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        try {
            Student savedStudent = service.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @Valid @RequestBody Student student) {
        try {
            System.out.println("Actualizando estudiante ID: " + id);
            System.out.println("Datos recibidos: " + student.toString());
            student.setId(id);
            Student updatedStudent = service.save(student);
            return ResponseEntity.ok(updatedStudent);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse(e.getMessage(), "Error al actualizar estudiante");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
    
    @GetMapping("/search")
    public ResponseEntity<StudentDetailDTO> searchByEnrollment(@RequestParam String enrollment) {
        try {
            StudentDetailDTO studentDetail = service.getStudentDetailByEnrollment(enrollment);
            return studentDetail != null ? ResponseEntity.ok(studentDetail) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/test-search/{enrollment}")
    public ResponseEntity<Student> testSearchByEnrollment(@PathVariable String enrollment) {
        try {
            Student student = service.findByEnrollment(enrollment);
            return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}