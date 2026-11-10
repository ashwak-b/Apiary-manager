package com.esb.apiary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esb.apiary.exception.DuplicateResourceException;
import com.esb.apiary.exception.ResourceNotFoundException;
import com.esb.apiary.model.Flora;
import com.esb.apiary.repository.FloraRepository;

@Service
@Transactional
public class FloraService {

    private final FloraRepository floraRepository;

    public FloraService(FloraRepository floraRepository) {
        this.floraRepository = floraRepository;
    }

    public List<Flora> findAll() {
        return floraRepository.findAll();
    }

    public Flora findById(Long id) {
        return floraRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Flora " + id + " not found"));
    }

    public Flora create(Flora flora) {
        if (floraRepository.existsByName(flora.getName())) {
            throw new DuplicateResourceException("Flora already exists: " + flora.getName());
        }
        flora.setId(null);
        return floraRepository.save(flora);
    }

    public Flora update(Long id, Flora details) {
        Flora existing = findById(id);
        if (!existing.getName().equals(details.getName())
                && floraRepository.existsByName(details.getName())) {
            throw new DuplicateResourceException("Flora already exists: " + details.getName());
        }
        existing.setName(details.getName());
        existing.setBloomingSeason(details.getBloomingSeason());
        return floraRepository.save(existing);
    }

    public void delete(Long id) {
        floraRepository.delete(findById(id));
    }
}