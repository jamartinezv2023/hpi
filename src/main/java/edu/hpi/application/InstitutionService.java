package edu.hpi.application;

import edu.hpi.domain.Institution;
import edu.hpi.domain.InstitutionName;
import edu.hpi.repository.InstitutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public Institution create(String name) {
        InstitutionName institutionName = InstitutionName.of(name);

        if (institutionRepository.existsByNameIgnoreCase(institutionName.value())) {
            throw new IllegalArgumentException("Institution name already exists");
        }

        Institution institution = new Institution(institutionName);
        return institutionRepository.save(institution);
    }

    @Transactional(readOnly = true)
    public Optional<Institution> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Institution id is required");
        }
        return institutionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Institution id is required");
        }
        return institutionRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Institution id is required");
        }
        institutionRepository.deleteById(id);
    }
}
