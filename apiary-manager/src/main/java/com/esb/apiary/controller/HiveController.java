package com.esb.apiary.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esb.apiary.model.Hive;
import com.esb.apiary.model.HiveStatus;
import com.esb.apiary.service.HiveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hives")
public class HiveController {

    private final HiveService service;

    public HiveController(HiveService service) {
        this.service = service;
    }

    @GetMapping
    public List<Hive> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Hive getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Hive> create(@Valid @RequestBody Hive hive) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(hive));
    }

    @PutMapping("/{id}")
    public Hive update(@PathVariable Long id, @Valid @RequestBody Hive hive) {
        return service.update(id, hive);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Custom query 2:  GET /api/hives/status/QUEENLESS
    @GetMapping("/status/{status}")
    public List<Hive> getByStatus(@PathVariable HiveStatus status) {
        return service.findByStatus(status);
    }

    // Custom query 3:  GET /api/hives/not-inspected?since=2026-10-01
    @GetMapping("/not-inspected")
    public List<Hive> getNotInspected(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since) {
        return service.findNotInspectedSince(since);
    }
}