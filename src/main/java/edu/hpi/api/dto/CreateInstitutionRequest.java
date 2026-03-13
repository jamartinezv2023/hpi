package edu.hpi.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateInstitutionRequest(
        @NotBlank String name
) {}
