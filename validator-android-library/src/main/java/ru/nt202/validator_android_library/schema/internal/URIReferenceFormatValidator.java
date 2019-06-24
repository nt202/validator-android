package ru.nt202.validator.everit.json.schema.internal;

import static java.lang.String.format;

import java.net.URI;
import java.net.URISyntaxException;
import java8.util.Optional;

public class URIReferenceFormatValidator extends AFormatValidator {

    @Override public Optional<String> validate(String subject) {
        try {
            new URI(subject);
            return Optional.empty();
        } catch (URISyntaxException e) {
            return failure(subject);
        }
    }

    protected Optional<String> failure(String subject) {
        return Optional.of(format("[%s] is not a valid URI reference", subject));
    }

    @Override public String formatName() {
        return "uri-reference";
    }
}
