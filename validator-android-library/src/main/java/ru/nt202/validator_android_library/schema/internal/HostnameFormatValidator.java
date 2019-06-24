package ru.nt202.validator_android_library.schema.internal;

import java8.util.Optional;
import org.apache.commons.validator.routines.DomainValidator;

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
