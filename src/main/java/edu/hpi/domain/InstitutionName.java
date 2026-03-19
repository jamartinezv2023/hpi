package edu.hpi.domain;

import java.util.Objects;

public final class InstitutionName {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 120;

    private final String value;

    private InstitutionName(String value) {
        this.value = value;
    }

    public static InstitutionName of(String rawValue) {
        if (rawValue == null || rawValue.trim().isBlank()) {
            throw new IllegalArgumentException("Institution name is required");
        }

        String normalizedValue = rawValue.trim();

        if (normalizedValue.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Institution name must contain at least 3 characters");
        }

        if (normalizedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Institution name exceeds the maximum allowed length");
        }

        return new InstitutionName(normalizedValue);
    }

    public String value() {
        return value;
    }

    public boolean sameValueAs(InstitutionName other) {
        return other != null && value.equalsIgnoreCase(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstitutionName that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
