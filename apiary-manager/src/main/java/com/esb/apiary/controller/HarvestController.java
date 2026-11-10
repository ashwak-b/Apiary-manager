package com.esb.apiary.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esb.apiary.model.Harvest;
import com.esb.apiary.service.HarvestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {

    private final HarvestService service;

    public HarvestController(HarvestService service) {
        this.service = service;
    }

    @GetMapping
    public List<Harvest> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Harvest getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Harvest> create(@Valid @RequestBody Harvest harvest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(harvest));
    }

    @PutMapping("/{id}")
    public Harvest update(@PathVariable Long id, @Valid @RequestBody Harvest harvest) {
        return service.update(id, harvest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Custom query 5:  GET /api/harvests/between?start=2026-01-01&end=2026-12-31
    @GetMapping("/between")
    public List<Harvest> getBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return service.findBetween(start, end);
    }

    // Custom query 6:  GET /api/harvests/total/apiary/1
    @GetMapping("/total/apiary/{apiaryId}")
    public Map<String, Object> getTotal(@PathVariable Long apiaryId) {
        return Map.of("apiaryId", apiaryId, "totalKg", service.totalHoneyByApiary(apiaryId));
    }
}