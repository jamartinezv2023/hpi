package edu.hpi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstitutionNameTest {

    @Nested
    @DisplayName("Valid institution names")
    class ValidInstitutionNames {

        @Test
        @DisplayName("should create institution name when value is valid")
        void shouldCreateInstitutionNameWhenValueIsValid() {
            InstitutionName institutionName = InstitutionName.of("Institución Educativa Cárdenas Centro");

            assertThat(institutionName.value())
                    .isEqualTo("Institución Educativa Cárdenas Centro");
        }

        @Test
        @DisplayName("should trim leading and trailing spaces")
        void shouldTrimLeadingAndTrailingSpaces() {
            InstitutionName institutionName = InstitutionName.of("   Institución Educativa Cárdenas Centro   ");

            assertThat(institutionName.value())
                    .isEqualTo("Institución Educativa Cárdenas Centro");
        }

        @Test
        @DisplayName("should allow minimum length")
        void shouldAllowMinimumLength() {
            InstitutionName institutionName = InstitutionName.of("ABC");

            assertThat(institutionName.value())
                    .isEqualTo("ABC");
        }

        @Test
        @DisplayName("should allow maximum length")
        void shouldAllowMaximumLength() {
            String validName = "A".repeat(120);

            InstitutionName institutionName = InstitutionName.of(validName);

            assertThat(institutionName.value())
                    .isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("Invalid institution names")
    class InvalidInstitutionNames {

        @Test
        @DisplayName("should reject null name")
        void shouldRejectNullName() {
            assertThatThrownBy(() -> InstitutionName.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name is required");
        }

        @Test
        @DisplayName("should reject blank name")
        void shouldRejectBlankName() {
            assertThatThrownBy(() -> InstitutionName.of("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name is required");
        }

        @Test
        @DisplayName("should reject name shorter than minimum length")
        void shouldRejectNameShorterThanMinimumLength() {
            assertThatThrownBy(() -> InstitutionName.of("AB"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name must contain at least 3 characters");
        }

        @Test
        @DisplayName("should reject name longer than maximum length")
        void shouldRejectNameLongerThanMaximumLength() {
            String invalidName = "A".repeat(121);

            assertThatThrownBy(() -> InstitutionName.of(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Institution name exceeds the maximum allowed length");
        }
    }

    @Nested
    @DisplayName("Value semantics")
    class ValueSemantics {

        @Test
        @DisplayName("should consider equal objects with same normalized value")
        void shouldConsiderEqualObjectsWithSameNormalizedValue() {
            InstitutionName first = InstitutionName.of("Institución Educativa Cárdenas Centro");
            InstitutionName second = InstitutionName.of("Institución Educativa Cárdenas Centro");

            assertThat(first)
                    .isEqualTo(second)
                    .hasSameHashCodeAs(second);
        }

        @Test
        @DisplayName("should compare same value ignoring case with sameValueAs")
        void shouldCompareSameValueIgnoringCaseWithSameValueAs() {
            InstitutionName first = InstitutionName.of("Institución Educativa Cárdenas Centro");
            InstitutionName second = InstitutionName.of("institución educativa cárdenas centro");

            assertThat(first.sameValueAs(second)).isTrue();
        }

        @Test
        @DisplayName("should return false when comparing against null")
        void shouldReturnFalseWhenComparingAgainstNull() {
            InstitutionName institutionName = InstitutionName.of("Institución Educativa Cárdenas Centro");

            assertThat(institutionName.sameValueAs(null)).isFalse();
        }

        @Test
        @DisplayName("should return normalized value in toString")
        void shouldReturnNormalizedValueInToString() {
            InstitutionName institutionName = InstitutionName.of("   Institución Educativa Cárdenas Centro   ");

            assertThat(institutionName.toString())
                    .isEqualTo("Institución Educativa Cárdenas Centro");
        }
    }
}
