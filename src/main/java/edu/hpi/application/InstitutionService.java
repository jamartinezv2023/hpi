package edu.hpi.application;

import edu.hpi.domain.Institution;
import edu.hpi.repository.InstitutionRepository;
import edu.hpi.service.InstitutionValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InstitutionService {

    private final InstitutionRepository repository;

    public InstitutionService(InstitutionRepository repository) {
        this.repository = repository;
    }

    public Institution create(String name) {
        InstitutionValidator.validateName(name);
        Institution institution = new Institution(name.trim());
        return repository.save(institution);
    }

    @Transactional(readOnly = true)
    public Optional<Institution> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Institution id is required");
        }
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Institution id is required");
        }
        return repository.existsById(id);
    }

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Institution id is required");
        }
        repository.deleteById(id);
    }
}
