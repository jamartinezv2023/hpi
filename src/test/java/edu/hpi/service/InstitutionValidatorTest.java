package edu.hpi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstitutionValidatorTest {

    @Nested
    @DisplayName("Valid institution names")
    class ValidInstitutionNames {

        @ParameterizedTest
        @ValueSource(strings = {
                "Institución Central",
                "Universidad Nacional",
                "Colegio Técnico Palmira",
                "Instituto Pedagógico Moderno"
        })
        void shouldAcceptValidNames(String name) {
            assertThatCode(() -> InstitutionValidator.validateName(name))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Invalid institution names")
    class InvalidInstitutionNames {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        void shouldRejectNullOrBlankNames(String name) {
            assertThatThrownBy(() -> InstitutionValidator.validateName(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name is required");
        }

        @ParameterizedTest
        @ValueSource(strings = {"A", "AB", "  A  "})
        void shouldRejectNamesShorterThanThreeCharacters(String name) {
            assertThatThrownBy(() -> InstitutionValidator.validateName(name))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name must contain at least 3 characters");
        }

        @Test
        void shouldRejectNamesLongerThan255Characters() {
            String longName = "A".repeat(256);

            assertThatThrownBy(() -> InstitutionValidator.validateName(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name must not exceed 255 characters");
        }
    }
}
