package com.esb.apiary.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esb.apiary.model.Inspection;
import com.esb.apiary.service.InspectionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    private final InspectionService service;

    public InspectionController(InspectionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Inspection> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Inspection getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Inspection> create(@Valid @RequestBody Inspection inspection) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(inspection));
    }

    @PutMapping("/{id}")
    public Inspection update(@PathVariable Long id, @Valid @RequestBody Inspection inspection) {
        return service.update(id, inspection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Custom query 4:  GET /api/inspections/hive/1
    @GetMapping("/hive/{hiveId}")
    public List<Inspection> getByHive(@PathVariable Long hiveId) {
        return service.findByHive(hiveId);
    }
}