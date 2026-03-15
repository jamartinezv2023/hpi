package edu.hpi.application;

import edu.hpi.domain.Institution;
import edu.hpi.repository.InstitutionRepository;
import edu.hpi.support.InstitutionTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstitutionServiceTest {

    private InstitutionRepository repository;
    private InstitutionService institutionService;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(InstitutionRepository.class);
        institutionService = new InstitutionService(repository);
    }

    @Test
    void shouldCreateInstitutionWhenNameIsValid() {
        Institution institution = InstitutionTestDataBuilder.anInstitution()
                .withName("Institución Pedagógica Central")
                .build();

        Mockito.when(repository.save(Mockito.any(Institution.class)))
                .thenReturn(institution);

        Institution result = institutionService.create("Institución Pedagógica Central");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Institución Pedagógica Central");
        assertThat(result.getCreatedAt()).isNotNull();

        Mockito.verify(repository).save(Mockito.any(Institution.class));
    }

    @Test
    void shouldTrimInstitutionNameBeforeSaving() {
        Institution institution = InstitutionTestDataBuilder.anInstitution()
                .withName("Institución Central")
                .build();

        Mockito.when(repository.save(Mockito.any(Institution.class)))
                .thenReturn(institution);

        Institution result = institutionService.create("   Institución Central   ");

        assertThat(result.getName()).isEqualTo("Institución Central");
        Mockito.verify(repository).save(Mockito.any(Institution.class));
    }

    @Test
    void shouldFindInstitutionByIdWhenExists() {
        Institution institution = InstitutionTestDataBuilder.anInstitution().build();

        Mockito.when(repository.findById(institution.getId()))
                .thenReturn(Optional.of(institution));

        Optional<Institution> result = institutionService.findById(institution.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(institution.getId());
        assertThat(result.get().getName()).isEqualTo(institution.getName());
    }

    @Test
    void shouldReturnEmptyWhenInstitutionDoesNotExist() {
        UUID id = UUID.randomUUID();

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Institution> result = institutionService.findById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenInstitutionExistsById() {
        UUID id = UUID.randomUUID();

        Mockito.when(repository.existsById(id))
                .thenReturn(true);

        boolean exists = institutionService.existsById(id);

        assertThat(exists).isTrue();
    }

    @Test
    void shouldDeleteInstitutionById() {
        UUID id = UUID.randomUUID();

        institutionService.deleteById(id);

        Mockito.verify(repository).deleteById(id);
    }

    @Test
    void shouldFailWhenCreateInstitutionWithBlankName() {
        assertThatThrownBy(() -> institutionService.create("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Institution name is required");
    }

    @Test
    void shouldFailWhenFindByIdWithNullId() {
        assertThatThrownBy(() -> institutionService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Institution id is required");
    }

    @Test
    void shouldFailWhenExistsByIdWithNullId() {
        assertThatThrownBy(() -> institutionService.existsById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Institution id is required");
    }

    @Test
    void shouldFailWhenDeleteByIdWithNullId() {
        assertThatThrownBy(() -> institutionService.deleteById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Institution id is required");
    }
}
