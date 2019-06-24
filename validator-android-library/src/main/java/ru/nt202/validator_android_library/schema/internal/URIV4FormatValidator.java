package ru.nt202.validator_android_library.schema.internal;

import java8.util.Optional;

import java.net.URI;
import java.net.URISyntaxException;

public class URIV4FormatValidator extends AFormatValidator {

    @Override
    public Optional<String> validate(final String subject) {
        try {
            URI uri = new URI(subject);
            return Optional.empty();
        } catch (URISyntaxException | NullPointerException e) {
            return failure(subject);
        }
    }

    protected Optional<String> failure(String subject) {
        return Optional.of(String.format("[%s] is not a valid URI", subject));
    }

    @Override public String formatName() {
        return "uri";
    }
}
