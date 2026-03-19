package edu.hpi.support;

import edu.hpi.domain.Institution;
import edu.hpi.domain.InstitutionName;

public class InstitutionTestDataBuilder {

    private String name = "Institución de Prueba";

    private InstitutionTestDataBuilder() {
    }

    public static InstitutionTestDataBuilder anInstitution() {
        return new InstitutionTestDataBuilder();
    }

    public InstitutionTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Institution build() {
        return new Institution(InstitutionName.of(name));
    }
}
