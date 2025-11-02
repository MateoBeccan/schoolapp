package com.backend.schoolapp.repository;

import com.backend.schoolapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
    Optional<Student> findByEnrollment(String enrollment);
    boolean existsByEnrollment(String enrollment);
    
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.subjects WHERE s.enrollment = :enrollment")
    Optional<Student> findByEnrollmentWithSubjects(@Param("enrollment") String enrollment);
}