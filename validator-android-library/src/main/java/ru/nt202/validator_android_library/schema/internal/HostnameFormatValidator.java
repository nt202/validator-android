package ru.nt202.validator.everit.json.schema.internal;

import org.apache.commons.validator.routines.DomainValidator;

import java8.util.Optional;

/**
 * Implementation of the "hostname" format value.
 */
public class HostnameFormatValidator extends AFormatValidator {

    @Override
    public Optional<String> validate(final String subject) {
        return DomainValidator.getInstance(true).isValid(subject) && !subject.contains("_") ?
                Optional.empty() :
                Optional.of(String.format("[%s] is not a valid hostname", subject));
    }

    @Override
    public String formatName() {
        return "hostname";
    }
}
