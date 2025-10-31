package com.backend.schoolapp.controller;

import com.backend.schoolapp.model.Teacher;
import com.backend.schoolapp.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {
    private final TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @GetMapping
    public List<Teacher> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Teacher getById(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    public Teacher create(@RequestBody Teacher teacher) { return service.save(teacher); }

    @PutMapping("/{id}")
    public Teacher update(@PathVariable Long id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        return service.save(teacher);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
