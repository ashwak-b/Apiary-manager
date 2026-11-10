package com.esb.apiary.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esb.apiary.model.Apiary;
import com.esb.apiary.service.ApiaryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/apiaries")
public class ApiaryController {

    private final ApiaryService service;

    public ApiaryController(ApiaryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Apiary> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Apiary getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Apiary> create(@Valid @RequestBody Apiary apiary) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(apiary));
    }

    @PutMapping("/{id}")
    public Apiary update(@PathVariable Long id, @Valid @RequestBody Apiary apiary) {
        return service.update(id, apiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Link a flora to an apiary (many-to-many)
    @PutMapping("/{apiaryId}/flora/{floraId}")
    public Apiary addFlora(@PathVariable Long apiaryId, @PathVariable Long floraId) {
        return service.addFlora(apiaryId, floraId);
    }

    // Custom query 1
    @GetMapping("/flora/{floraName}")
    public List<Apiary> getByFlora(@PathVariable String floraName) {
        return service.findByFloraName(floraName);
    }
}