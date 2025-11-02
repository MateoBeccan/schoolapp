package com.backend.schoolapp.repository;

import com.backend.schoolapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByEmail(String email);
    boolean existsByEnrollment(String enrollment);
    Optional<Teacher> findByEnrollment(String enrollment);
    
    @Query("SELECT t FROM Teacher t WHERE t.enrollment LIKE %:query% OR t.firstName LIKE %:query% OR t.lastName LIKE %:query%")
    List<Teacher> searchByEnrollmentOrName(@Param("query") String query);
}
