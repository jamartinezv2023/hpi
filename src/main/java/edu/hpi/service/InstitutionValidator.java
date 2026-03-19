package edu.hpi.service;

import edu.hpi.domain.InstitutionName;

public final class InstitutionValidator {

    private InstitutionValidator() {
    }

    public static void validateName(String name) {
        InstitutionName.of(name);
    }
}
