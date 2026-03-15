package edu.hpi.repository;

import edu.hpi.AbstractIntegrationTest;
import edu.hpi.domain.Institution;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class InstitutionRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Test
    void shouldSaveInstitution() {
        Institution institution = new Institution("Institución de Integración");

        Institution savedInstitution = institutionRepository.save(institution);

        assertThat(savedInstitution).isNotNull();
        assertThat(savedInstitution.getId()).isNotNull();
        assertThat(savedInstitution.getName()).isEqualTo("Institución de Integración");
        assertThat(savedInstitution.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldFindInstitutionById() {
        Institution institution = new Institution("Universidad de Persistencia");
        Institution savedInstitution = institutionRepository.save(institution);

        Optional<Institution> foundInstitution = institutionRepository.findById(savedInstitution.getId());

        assertThat(foundInstitution).isPresent();
        assertThat(foundInstitution.get().getId()).isEqualTo(savedInstitution.getId());
        assertThat(foundInstitution.get().getName()).isEqualTo("Universidad de Persistencia");
    }

    @Test
    void shouldCheckIfInstitutionExistsById() {
        Institution institution = new Institution("Colegio Existente");
        Institution savedInstitution = institutionRepository.save(institution);

        boolean exists = institutionRepository.existsById(savedInstitution.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldDeleteInstitutionById() {
        Institution institution = new Institution("Institución Eliminable");
        Institution savedInstitution = institutionRepository.save(institution);
        UUID institutionId = savedInstitution.getId();

        institutionRepository.deleteById(institutionId);

        Optional<Institution> deletedInstitution = institutionRepository.findById(institutionId);

        assertThat(deletedInstitution).isEmpty();
    }
}
