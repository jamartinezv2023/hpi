package edu.hpi.repository;

import edu.hpi.domain.Institution;
import edu.hpi.domain.InstitutionName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InstitutionRepositoryTest {

    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withStartupTimeout(Duration.ofMinutes(3));

    static {
        POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL_CONTAINER::getDriverClassName);

        registry.add("spring.flyway.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.flyway.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @Autowired
    private InstitutionRepository institutionRepository;

    @Test
    void shouldSaveInstitution() {
        Institution institution =
                new Institution(InstitutionName.of("Institución de Integración"));

        Institution saved = institutionRepository.save(institution);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Institución de Integración");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldFindById() {
        Institution institution =
                new Institution(InstitutionName.of("Universidad de Persistencia"));

        Institution saved = institutionRepository.save(institution);

        Optional<Institution> result = institutionRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getName()).isEqualTo("Universidad de Persistencia");
    }

    @Test
    void shouldVerifyExistenceById() {
        Institution institution =
                new Institution(InstitutionName.of("Colegio Existente"));

        Institution saved = institutionRepository.save(institution);

        boolean exists = institutionRepository.existsById(saved.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldDeleteById() {
        Institution institution =
                new Institution(InstitutionName.of("Institución Eliminable"));

        Institution saved = institutionRepository.save(institution);

        institutionRepository.deleteById(saved.getId());

        boolean exists = institutionRepository.existsById(saved.getId());

        assertThat(exists).isFalse();
    }
}
