package edu.hpi.application;

import edu.hpi.domain.Institution;
import edu.hpi.repository.InstitutionRepository;
import edu.hpi.support.InstitutionTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @InjectMocks
    private InstitutionService institutionService;

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should create institution with normalized name")
        void shouldCreateInstitutionWithNormalizedName() {
            String rawName = "   Institución Pedagógica Central   ";

            Institution savedInstitution = InstitutionTestDataBuilder.anInstitution()
                    .withName("Institución Pedagógica Central")
                    .build();

            given(institutionRepository.existsByNameIgnoreCase("Institución Pedagógica Central"))
                    .willReturn(false);

            given(institutionRepository.save(any(Institution.class)))
                    .willReturn(savedInstitution);

            Institution result = institutionService.create(rawName);

            ArgumentCaptor<Institution> institutionCaptor = ArgumentCaptor.forClass(Institution.class);
            verify(institutionRepository).save(institutionCaptor.capture());

            Institution institutionToSave = institutionCaptor.getValue();

            assertThat(institutionToSave.getName()).isEqualTo("Institución Pedagógica Central");
            assertThat(result).isSameAs(savedInstitution);
        }

        @Test
        @DisplayName("should reject blank name")
        void shouldRejectBlankName() {
            assertThatThrownBy(() -> institutionService.create("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name is required");

            verify(institutionRepository, never()).save(any(Institution.class));
        }

        @Test
        @DisplayName("should reject name shorter than minimum length")
        void shouldRejectNameShorterThanMinimumLength() {
            assertThatThrownBy(() -> institutionService.create("AB"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name must contain at least 3 characters");

            verify(institutionRepository, never()).save(any(Institution.class));
        }

        @Test
        @DisplayName("should reject name longer than maximum length")
        void shouldRejectNameLongerThanMaximumLength() {
            String longName = "A".repeat(121);

            assertThatThrownBy(() -> institutionService.create(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name exceeds the maximum allowed length");

            verify(institutionRepository, never()).save(any(Institution.class));
        }

        @Test
        @DisplayName("should reject duplicated institution name")
        void shouldRejectDuplicatedInstitutionName() {
            given(institutionRepository.existsByNameIgnoreCase("Institución Central"))
                    .willReturn(true);

            assertThatThrownBy(() -> institutionService.create("Institución Central"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name already exists");

            verify(institutionRepository, never()).save(any(Institution.class));
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return institution when id exists")
        void shouldReturnInstitutionWhenIdExists() {
            Institution institution = InstitutionTestDataBuilder.anInstitution()
                    .withName("Institución de Consulta")
                    .build();

            given(institutionRepository.findById(institution.getId()))
                    .willReturn(Optional.of(institution));

            Optional<Institution> result = institutionService.findById(institution.getId());

            assertThat(result).isPresent();
            assertThat(result.get()).isSameAs(institution);
        }

        @Test
        @DisplayName("should return empty when id does not exist")
        void shouldReturnEmptyWhenIdDoesNotExist() {
            UUID id = UUID.randomUUID();

            given(institutionRepository.findById(id))
                    .willReturn(Optional.empty());

            Optional<Institution> result = institutionService.findById(id);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should reject null id")
        void shouldRejectNullId() {
            assertThatThrownBy(() -> institutionService.findById(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution id is required");
        }
    }

    @Nested
    @DisplayName("existsById")
    class ExistsById {

        @Test
        @DisplayName("should return true when institution exists")
        void shouldReturnTrueWhenInstitutionExists() {
            UUID id = UUID.randomUUID();

            given(institutionRepository.existsById(id))
                    .willReturn(true);

            boolean result = institutionService.existsById(id);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("should return false when institution does not exist")
        void shouldReturnFalseWhenInstitutionDoesNotExist() {
            UUID id = UUID.randomUUID();

            given(institutionRepository.existsById(id))
                    .willReturn(false);

            boolean result = institutionService.existsById(id);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should reject null id")
        void shouldRejectNullId() {
            assertThatThrownBy(() -> institutionService.existsById(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution id is required");
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("should delegate delete to repository")
        void shouldDelegateDeleteToRepository() {
            UUID id = UUID.randomUUID();

            institutionService.deleteById(id);

            verify(institutionRepository).deleteById(eq(id));
        }

        @Test
        @DisplayName("should reject null id")
        void shouldRejectNullId() {
            assertThatThrownBy(() -> institutionService.deleteById(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution id is required");
        }
    }
}
