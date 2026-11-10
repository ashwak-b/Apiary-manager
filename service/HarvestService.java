package com.esb.apiary.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esb.apiary.exception.BadRequestException;
import com.esb.apiary.exception.ResourceNotFoundException;
import com.esb.apiary.model.Harvest;
import com.esb.apiary.model.Hive;
import com.esb.apiary.model.HiveStatus;
import com.esb.apiary.repository.ApiaryRepository;
import com.esb.apiary.repository.HarvestRepository;
import com.esb.apiary.repository.HiveRepository;

@Service
@Transactional
public class HarvestService {

    private final HarvestRepository harvestRepository;
    private final HiveRepository hiveRepository;
    private final ApiaryRepository apiaryRepository;

    public HarvestService(HarvestRepository harvestRepository,
                          HiveRepository hiveRepository,
                          ApiaryRepository apiaryRepository) {
        this.harvestRepository = harvestRepository;
        this.hiveRepository = hiveRepository;
        this.apiaryRepository = apiaryRepository;
    }

    public List<Harvest> findAll() {
        return harvestRepository.findAll();
    }

    public Harvest findById(Long id) {
        return harvestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Harvest " + id + " not found"));
    }

    public Harvest create(Harvest harvest) {
        if (harvest.getHive() == null || harvest.getHive().getId() == null) {
            throw new BadRequestException("hive.id is required");
        }
        Hive hive = hiveRepository.findById(harvest.getHive().getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Hive " + harvest.getHive().getId() + " not found"));
        if (hive.getStatus() == HiveStatus.DEAD) {
            throw new BadRequestException("Cannot harvest a dead hive");
        }
        harvest.setId(null);
        harvest.setHive(hive);
        return harvestRepository.save(harvest);
    }

    public Harvest update(Long id, Harvest details) {
        Harvest existing = findById(id);
        existing.setHarvestDate(details.getHarvestDate());
        existing.setQuantityKg(details.getQuantityKg());
        existing.setHoneyType(details.getHoneyType());
        return harvestRepository.save(existing);
    }

    public void delete(Long id) {
        harvestRepository.delete(findById(id));
    }

    // Custom query 5
    public List<Harvest> findBetween(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("start date must be before end date");
        }
        return harvestRepository.findByHarvestDateBetween(start, end);
    }

    // Custom query 6
    public Double totalHoneyByApiary(Long apiaryId) {
        if (!apiaryRepository.existsById(apiaryId)) {
            throw new ResourceNotFoundException("Apiary " + apiaryId + " not found");
        }
        return harvestRepository.totalHoneyByApiary(apiaryId);
    }
}