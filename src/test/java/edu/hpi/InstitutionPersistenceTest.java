package edu.hpi;

import edu.hpi.application.InstitutionService;
import edu.hpi.repository.InstitutionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class InstitutionPersistenceTest extends AbstractIntegrationTest {

    @Autowired
    private InstitutionService service;

    @Autowired
    private InstitutionRepository repository;

    @Test
    void shouldPersistInstitutionThroughService() {
        var created = service.create("HPI University");

        assertThat(created.getId()).isNotNull();
        assertThat(repository.findById(created.getId())).isPresent();
    }
}
