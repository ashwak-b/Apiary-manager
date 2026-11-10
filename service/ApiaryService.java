package com.esb.apiary.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esb.apiary.exception.BadRequestException;
import com.esb.apiary.exception.ResourceNotFoundException;
import com.esb.apiary.model.Apiary;
import com.esb.apiary.model.Beekeeper;
import com.esb.apiary.model.Flora;
import com.esb.apiary.repository.ApiaryRepository;
import com.esb.apiary.repository.BeekeeperRepository;
import com.esb.apiary.repository.FloraRepository;

@Service
@Transactional
public class ApiaryService {

    private final ApiaryRepository apiaryRepository;
    private final BeekeeperRepository beekeeperRepository;
    private final FloraRepository floraRepository;

    public ApiaryService(ApiaryRepository apiaryRepository,
                         BeekeeperRepository beekeeperRepository,
                         FloraRepository floraRepository) {
        this.apiaryRepository = apiaryRepository;
        this.beekeeperRepository = beekeeperRepository;
        this.floraRepository = floraRepository;
    }

    public List<Apiary> findAll() {
        return apiaryRepository.findAll();
    }

    public Apiary findById(Long id) {
        return apiaryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Apiary " + id + " not found"));
    }

    public Apiary create(Apiary apiary) {
        apiary.setId(null);
        apiary.setBeekeeper(loadBeekeeper(apiary.getBeekeeper()));
        apiary.setFlora(loadFlora(apiary.getFlora()));
        return apiaryRepository.save(apiary);
    }

    public Apiary update(Long id, Apiary details) {
        Apiary existing = findById(id);
        existing.setName(details.getName());
        existing.setLocation(details.getLocation());
        existing.setBeekeeper(loadBeekeeper(details.getBeekeeper()));
        existing.setFlora(loadFlora(details.getFlora()));
        return apiaryRepository.save(existing);
    }

    public void delete(Long id) {
        apiaryRepository.delete(findById(id));
    }

    // Many-to-many: link one flora to one apiary
    public Apiary addFlora(Long apiaryId, Long floraId) {
        Apiary apiary = findById(apiaryId);
        Flora flora = floraRepository.findById(floraId)
            .orElseThrow(() -> new ResourceNotFoundException("Flora " + floraId + " not found"));
        apiary.getFlora().add(flora);
        return apiaryRepository.save(apiary);
    }

    // Custom query 1
    public List<Apiary> findByFloraName(String floraName) {
        return apiaryRepository.findByFloraName(floraName);
    }

    private Beekeeper loadBeekeeper(Beekeeper ref) {
        if (ref == null || ref.getId() == null) {
            throw new BadRequestException("beekeeper.id is required");
        }
        return beekeeperRepository.findById(ref.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Beekeeper " + ref.getId() + " not found"));
    }

    private Set<Flora> loadFlora(Set<Flora> refs) {
        Set<Flora> result = new HashSet<>();
        if (refs != null) {
            for (Flora ref : refs) {
                if (ref.getId() == null) {
                    throw new BadRequestException("Each flora needs an id");
                }
                result.add(floraRepository.findById(ref.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Flora " + ref.getId() + " not found")));
            }
        }
        return result;
    }
}