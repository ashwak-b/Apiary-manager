package com.esb.apiary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esb.apiary.exception.BadRequestException;
import com.esb.apiary.exception.ResourceNotFoundException;
import com.esb.apiary.model.HealthLevel;
import com.esb.apiary.model.Hive;
import com.esb.apiary.model.HiveStatus;
import com.esb.apiary.model.Inspection;
import com.esb.apiary.repository.HiveRepository;
import com.esb.apiary.repository.InspectionRepository;

@Service
@Transactional
public class InspectionService {

    private final InspectionRepository inspectionRepository;
    private final HiveRepository hiveRepository;

    public InspectionService(InspectionRepository inspectionRepository, HiveRepository hiveRepository) {
        this.inspectionRepository = inspectionRepository;
        this.hiveRepository = hiveRepository;
    }

    public List<Inspection> findAll() {
        return inspectionRepository.findAll();
    }

    public Inspection findById(Long id) {
        return inspectionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inspection " + id + " not found"));
    }

    public Inspection create(Inspection inspection) {
        if (inspection.getHive() == null || inspection.getHive().getId() == null) {
            throw new BadRequestException("hive.id is required");
        }
        Hive hive = hiveRepository.findById(inspection.getHive().getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Hive " + inspection.getHive().getId() + " not found"));

        // Business rule 1
        if (hive.getStatus() == HiveStatus.DEAD) {
            throw new BadRequestException("Cannot inspect a dead hive");
        }
        // Business rule 2
        if (inspection.getHealthLevel() == HealthLevel.POOR && hive.getStatus() == HiveStatus.ACTIVE) {
            hive.setStatus(HiveStatus.WEAK);
        }
        inspection.setId(null);
        inspection.setHive(hive);
        return inspectionRepository.save(inspection);
    }

    public Inspection update(Long id, Inspection details) {
        Inspection existing = findById(id);
        existing.setInspectionDate(details.getInspectionDate());
        existing.setHealthLevel(details.getHealthLevel());
        existing.setNotes(details.getNotes());
        return inspectionRepository.save(existing);
    }

    public void delete(Long id) {
        inspectionRepository.delete(findById(id));
    }

    // Custom query 4
    public List<Inspection> findByHive(Long hiveId) {
        if (!hiveRepository.existsById(hiveId)) {
            throw new ResourceNotFoundException("Hive " + hiveId + " not found");
        }
        return inspectionRepository.findByHiveIdOrderByInspectionDateDesc(hiveId);
    }
}