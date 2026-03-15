package edu.hpi.support;

import edu.hpi.domain.Institution;

public class InstitutionTestDataBuilder {

    private String name = "Institución de Prueba";

    public InstitutionTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Institution build() {
        return new Institution(name);
    }

    public static InstitutionTestDataBuilder anInstitution() {
        return new InstitutionTestDataBuilder();
    }
}
