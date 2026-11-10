package com.esb.apiary.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esb.apiary.exception.BadRequestException;
import com.esb.apiary.exception.DuplicateResourceException;
import com.esb.apiary.exception.ResourceNotFoundException;
import com.esb.apiary.model.Apiary;
import com.esb.apiary.model.Hive;
import com.esb.apiary.model.HiveStatus;
import com.esb.apiary.repository.ApiaryRepository;
import com.esb.apiary.repository.HiveRepository;

@Service
@Transactional
public class HiveService {

    private final HiveRepository hiveRepository;
    private final ApiaryRepository apiaryRepository;

    public HiveService(HiveRepository hiveRepository, ApiaryRepository apiaryRepository) {
        this.hiveRepository = hiveRepository;
        this.apiaryRepository = apiaryRepository;
    }

    public List<Hive> findAll() {
        return hiveRepository.findAll();
    }

    public Hive findById(Long id) {
        return hiveRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Hive " + id + " not found"));
    }

    public Hive create(Hive hive) {
        if (hiveRepository.existsByCode(hive.getCode())) {
            throw new DuplicateResourceException("Hive code already used: " + hive.getCode());
        }
        hive.setId(null);
        hive.setApiary(loadApiary(hive.getApiary()));
        return hiveRepository.save(hive);
    }

    public Hive update(Long id, Hive details) {
        Hive existing = findById(id);
        if (!existing.getCode().equals(details.getCode())
                && hiveRepository.existsByCode(details.getCode())) {
            throw new DuplicateResourceException("Hive code already used: " + details.getCode());
        }
        existing.setCode(details.getCode());
        existing.setStatus(details.getStatus());
        existing.setInstallDate(details.getInstallDate());
        existing.setApiary(loadApiary(details.getApiary()));
        return hiveRepository.save(existing);
    }

    public void delete(Long id) {
        hiveRepository.delete(findById(id));
    }

    // Custom query 2
    public List<Hive> findByStatus(HiveStatus status) {
        return hiveRepository.findByStatus(status);
    }

    // Custom query 3
    public List<Hive> findNotInspectedSince(LocalDate since) {
        return hiveRepository.findHivesNotInspectedSince(since);
    }

    private Apiary loadApiary(Apiary ref) {
        if (ref == null || ref.getId() == null) {
            throw new BadRequestException("apiary.id is required");
        }
        return apiaryRepository.findById(ref.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Apiary " + ref.getId() + " not found"));
    }
}