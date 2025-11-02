package com.backend.schoolapp.controller;

import com.backend.schoolapp.model.Career;
import com.backend.schoolapp.service.CareerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CareerController {
    private final CareerService service;

    @GetMapping
    public ResponseEntity<List<Career>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Career> getById(@PathVariable Long id) {
        Career career = service.getById(id);
        return career != null ? ResponseEntity.ok(career) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Career> create(@Valid @RequestBody Career career) {
        try {
            Career saved = service.save(career);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Career> update(@PathVariable Long id, @Valid @RequestBody Career career) {
        career.setId(id);
        return ResponseEntity.ok(service.save(career));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Career controller working");
    }
}