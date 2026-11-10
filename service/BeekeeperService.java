package com.esb.apiary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esb.apiary.exception.DuplicateResourceException;
import com.esb.apiary.exception.ResourceNotFoundException;
import com.esb.apiary.model.Beekeeper;
import com.esb.apiary.repository.BeekeeperRepository;

@Service
@Transactional
public class BeekeeperService {

    private final BeekeeperRepository beekeeperRepository;

    public BeekeeperService(BeekeeperRepository beekeeperRepository) {
        this.beekeeperRepository = beekeeperRepository;
    }

    public List<Beekeeper> findAll() {
        return beekeeperRepository.findAll();
    }

    public Beekeeper findById(Long id) {
        return beekeeperRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Beekeeper " + id + " not found"));
    }

    public Beekeeper create(Beekeeper beekeeper) {
        if (beekeeperRepository.existsByEmail(beekeeper.getEmail())) {
            throw new DuplicateResourceException("Email already used: " + beekeeper.getEmail());
        }
        beekeeper.setId(null);
        return beekeeperRepository.save(beekeeper);
    }

    public Beekeeper update(Long id, Beekeeper details) {
        Beekeeper existing = findById(id);
        if (!existing.getEmail().equals(details.getEmail())
                && beekeeperRepository.existsByEmail(details.getEmail())) {
            throw new DuplicateResourceException("Email already used: " + details.getEmail());
        }
        existing.setFullName(details.getFullName());
        existing.setEmail(details.getEmail());
        existing.setPhone(details.getPhone());
        return beekeeperRepository.save(existing);
    }

    public void delete(Long id) {
        beekeeperRepository.delete(findById(id));
    }
}