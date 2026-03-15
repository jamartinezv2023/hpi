package edu.hpi.service;

public final class InstitutionValidator {

    private InstitutionValidator() {
    }

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Institution name is required");
        }

        if (name.trim().length() < 3) {
            throw new IllegalArgumentException("Institution name must contain at least 3 characters");
        }

        if (name.trim().length() > 255) {
            throw new IllegalArgumentException("Institution name must not exceed 255 characters");
        }
    }
}
