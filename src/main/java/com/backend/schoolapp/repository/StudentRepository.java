package com.backend.schoolapp.repository;

import com.backend.schoolapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}