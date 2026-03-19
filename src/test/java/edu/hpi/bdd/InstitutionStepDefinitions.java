package edu.hpi.bdd;

import edu.hpi.application.InstitutionService;
import edu.hpi.domain.Institution;
import edu.hpi.domain.InstitutionName;
import edu.hpi.repository.InstitutionRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InstitutionStepDefinitions {

    private final InstitutionService institutionService;
    private final InstitutionRepository institutionRepository;

    private Institution createdInstitution;
    private Exception capturedException;
    private boolean authenticatedAdministrator;

    @Autowired
    public InstitutionStepDefinitions(
            InstitutionService institutionService,
            InstitutionRepository institutionRepository
    ) {
        this.institutionService = institutionService;
        this.institutionRepository = institutionRepository;
    }

    @Before
    public void cleanDatabase() {
        institutionRepository.deleteAll();
        createdInstitution = null;
        capturedException = null;
        authenticatedAdministrator = false;
    }

    @Given("an authenticated administrator")
    public void an_authenticated_administrator() {
        authenticatedAdministrator = true;
        createdInstitution = null;
        capturedException = null;
    }

    @Given("no institution exists with name {string}")
    public void no_institution_exists_with_name(String name) {
        createdInstitution = null;
        capturedException = null;
    }

    @Given("an institution exists with name {string}")
    public void an_institution_exists_with_name(String name) {
        institutionRepository.save(
                new Institution(InstitutionName.of(name.trim()))
        );

        createdInstitution = null;
        capturedException = null;
    }

    @When("the administrator registers an institution with name {string}")
    public void the_administrator_registers_an_institution_with_name(String name) {
        assertTrue(authenticatedAdministrator, "An authenticated administrator is required");

        createdInstitution = null;
        capturedException = null;

        try {
            createdInstitution = institutionService.create(name);
        } catch (Exception exception) {
            capturedException = exception;
        }
    }

    @Then("the institution is stored in the system")
    public void the_institution_is_stored_in_the_system() {
        assertNotNull(createdInstitution, "The institution should have been created");
        assertNotNull(createdInstitution.getId(), "The institution id should not be null");

        Optional<Institution> storedInstitution =
                institutionRepository.findById(createdInstitution.getId());

        assertTrue(storedInstitution.isPresent(), "The institution should exist in the repository");
    }

    @Then("the institution has a generated identifier")
    public void the_institution_has_a_generated_identifier() {
        assertNotNull(createdInstitution, "The institution should have been created");
        assertNotNull(createdInstitution.getId(), "The institution should have a generated identifier");
    }

    @Then("the institution name is {string}")
    public void the_institution_name_is(String expectedName) {
        assertNotNull(createdInstitution, "The institution should have been created");
        assertEquals(expectedName, createdInstitution.getName());
    }

    @Then("the stored institution name is {string}")
    public void the_stored_institution_name_is(String expectedName) {
        assertNotNull(createdInstitution, "The institution should have been created");

        Institution storedInstitution = institutionRepository.findById(createdInstitution.getId())
                .orElseThrow(() -> new AssertionError("The stored institution was not found"));

        assertEquals(expectedName, storedInstitution.getName());
    }

    @Then("the system rejects the registration")
    public void the_system_rejects_the_registration() {
        assertNull(createdInstitution, "No institution should be created when validation fails");
        assertNotNull(capturedException, "An exception should have been captured");
        assertInstanceOf(IllegalArgumentException.class, capturedException);
    }

    @Then("the validation message is {string}")
    public void the_validation_message_is(String expectedMessage) {
        assertNotNull(capturedException, "An exception should have been captured");
        assertEquals(expectedMessage, capturedException.getMessage());
    }
}
