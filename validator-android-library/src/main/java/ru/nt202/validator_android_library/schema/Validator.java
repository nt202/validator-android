package ru.nt202.validator.everit.json.schema;

public interface Validator {

    void performValidation(Schema schema, Object input);

}


