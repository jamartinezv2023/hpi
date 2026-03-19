package edu.hpi;

import edu.hpi.domain.Institution;
import edu.hpi.domain.InstitutionName;
import edu.hpi.repository.InstitutionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InstitutionPersistenceTest extends AbstractIntegrationTest {

    @Autowired
    private InstitutionRepository repository;

    @Test
    void shouldPersistInstitutionSuccessfully() {

        Institution institution =
                new Institution(InstitutionName.of("Institución Test"));

        repository.save(institution);

        Optional<Institution> result =
                repository.findById(institution.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName())
                .isEqualTo("Institución Test");
    }

    @Test
    void shouldFindInstitutionById() {

        Institution institution =
                new Institution(InstitutionName.of("Institución Find"));

        repository.save(institution);

        Optional<Institution> result =
                repository.findById(institution.getId());

        assertThat(result).isPresent();
    }

    @Test
    void shouldDeleteInstitution() {

        Institution institution =
                new Institution(InstitutionName.of("Institución Delete"));

        repository.save(institution);

        UUID id = institution.getId();

        repository.deleteById(id);

        Optional<Institution> result =
                repository.findById(id);

        assertThat(result).isEmpty();
    }

}