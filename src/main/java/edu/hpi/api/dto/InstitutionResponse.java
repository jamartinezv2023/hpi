package edu.hpi.api.dto;

import java.util.UUID;

public record InstitutionResponse(
        UUID id,
        String name
) {}
