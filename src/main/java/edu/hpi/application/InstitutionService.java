package edu.hpi.application;

import edu.hpi.domain.Institution;
import edu.hpi.repository.InstitutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InstitutionService {

    private final InstitutionRepository repository;

    public InstitutionService(InstitutionRepository repository) {
        this.repository = repository;
    }

    public Institution create(String name) {
        Institution institution = new Institution(name);
        return repository.save(institution);
    }
}
