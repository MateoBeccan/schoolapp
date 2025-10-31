package com.backend.schoolapp.controller;

import com.backend.schoolapp.model.Subject;
import com.backend.schoolapp.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {
    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public List<Subject> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Subject getById(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    public Subject create(@RequestBody Subject subject) { return service.save(subject); }

    @PutMapping("/{id}")
    public Subject update(@PathVariable Long id, @RequestBody Subject subject) {
        subject.setId(id);
        return service.save(subject);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}