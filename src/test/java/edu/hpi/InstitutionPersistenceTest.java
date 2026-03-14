package edu.hpi;

import edu.hpi.domain.Institution;
import edu.hpi.repository.InstitutionRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class InstitutionPersistenceTest extends AbstractIntegrationTest {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Test
    void shouldPersistInstitution() {
        Institution institution = new Institution("Institución Pedagógica Central");

        Institution savedInstitution = institutionRepository.save(institution);

        assertThat(savedInstitution).isNotNull();
        assertThat(savedInstitution.getId()).isNotNull();
        assertThat(savedInstitution.getName()).isEqualTo("Institución Pedagógica Central");
        assertThat(savedInstitution.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldFindInstitutionById() {
        Institution institution = new Institution("Universidad de Integración");

        Institution savedInstitution = institutionRepository.save(institution);
        UUID institutionId = savedInstitution.getId();

        Institution foundInstitution = institutionRepository.findById(institutionId).orElse(null);

        assertThat(foundInstitution).isNotNull();
        assertThat(foundInstitution.getId()).isEqualTo(institutionId);
        assertThat(foundInstitution.getName()).isEqualTo("Universidad de Integración");
        assertThat(foundInstitution.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldDeleteInstitution() {
        Institution institution = new Institution("Institución Temporal");

        Institution savedInstitution = institutionRepository.save(institution);
        UUID institutionId = savedInstitution.getId();

        institutionRepository.deleteById(institutionId);

        boolean exists = institutionRepository.findById(institutionId).isPresent();

        assertThat(exists).isFalse();
    }
}
