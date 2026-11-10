package com.esb.apiary.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.esb.apiary.model.Beekeeper;
import com.esb.apiary.service.BeekeeperService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/beekeepers")
public class BeekeeperController {

    private final BeekeeperService service;

    public BeekeeperController(BeekeeperService service) {
        this.service = service;
    }

    @GetMapping
    public List<Beekeeper> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Beekeeper getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Beekeeper> create(@Valid @RequestBody Beekeeper beekeeper) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(beekeeper));
    }

    @PutMapping("/{id}")
    public Beekeeper update(@PathVariable Long id, @Valid @RequestBody Beekeeper beekeeper) {
        return service.update(id, beekeeper);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}