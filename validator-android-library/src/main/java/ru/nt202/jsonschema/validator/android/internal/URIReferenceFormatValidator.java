package ru.nt202.jsonschema.validator.android.internal;

import java8.util.Optional;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

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
