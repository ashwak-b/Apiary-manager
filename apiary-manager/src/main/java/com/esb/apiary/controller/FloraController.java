package com.esb.apiary.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esb.apiary.model.Flora;
import com.esb.apiary.service.FloraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/flora")
public class FloraController {

    private final FloraService service;

    public FloraController(FloraService service) {
        this.service = service;
    }

    @GetMapping
    public List<Flora> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Flora getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Flora> create(@Valid @RequestBody Flora flora) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(flora));
    }

    @PutMapping("/{id}")
    public Flora update(@PathVariable Long id, @Valid @RequestBody Flora flora) {
        return service.update(id, flora);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}