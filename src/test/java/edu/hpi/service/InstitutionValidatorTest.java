package edu.hpi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("InstitutionValidator")
class InstitutionValidatorTest {

    @Nested
    @DisplayName("Valid institution names")
    class ValidInstitutionNames {

        @Test
        @DisplayName("should accept a valid institution name")
        void shouldAcceptValidInstitutionName() {
            assertThatCode(() -> InstitutionValidator.validateName("Institución Educativa Central"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should accept a valid institution name with leading and trailing spaces")
        void shouldAcceptValidInstitutionNameWithSpaces() {
            assertThatCode(() -> InstitutionValidator.validateName("   Institución Educativa Central   "))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should accept a name with exactly 3 characters")
        void shouldAcceptNameWithMinimumLength() {
            assertThatCode(() -> InstitutionValidator.validateName("ABC"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should accept a name with exactly 120 characters")
        void shouldAcceptNameWithMaximumLength() {
            String name = "A".repeat(120);

            assertThatCode(() -> InstitutionValidator.validateName(name))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Invalid institution names")
    class InvalidInstitutionNames {

        @Test
        @DisplayName("should reject null name")
        void shouldRejectNullName() {
            assertThatThrownBy(() -> InstitutionValidator.validateName(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name is required");
        }

        @Test
        @DisplayName("should reject blank name")
        void shouldRejectBlankName() {
            assertThatThrownBy(() -> InstitutionValidator.validateName("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name is required");
        }

        @Test
        @DisplayName("should reject empty name")
        void shouldRejectEmptyName() {
            assertThatThrownBy(() -> InstitutionValidator.validateName(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name is required");
        }

        @Test
        @DisplayName("should reject name shorter than 3 characters")
        void shouldRejectNameShorterThanMinimumLength() {
            assertThatThrownBy(() -> InstitutionValidator.validateName("AB"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name must contain at least 3 characters");
        }

        @Test
        @DisplayName("should reject trimmed name shorter than 3 characters")
        void shouldRejectTrimmedNameShorterThanMinimumLength() {
            assertThatThrownBy(() -> InstitutionValidator.validateName("  AB  "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name must contain at least 3 characters");
        }

        @Test
        @DisplayName("should reject name longer than 120 characters")
        void shouldRejectNameLongerThanMaximumLength() {
            String name = "A".repeat(121);

            assertThatThrownBy(() -> InstitutionValidator.validateName(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name exceeds the maximum allowed length");
        }
    }
}
