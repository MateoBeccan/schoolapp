package com.backend.schoolapp.model;

import lombok.Data;
import java.util.List;

@Data
public class StudentDetailDTO {
    private Long id;
    private String enrollment;
    private String firstName;
    private String lastName;
    private String email;
    private Career career;
    private List<Subject> subjects;
    private List<Exam> exams;
    
    public StudentDetailDTO(Student student, List<Exam> exams) {
        this.id = student.getId();
        this.enrollment = student.getEnrollment();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.career = student.getCareer();
        this.subjects = student.getSubjects();
        this.exams = exams;
    }
}