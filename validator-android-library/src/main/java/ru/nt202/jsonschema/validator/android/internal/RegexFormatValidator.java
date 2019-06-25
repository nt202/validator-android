package ru.nt202.jsonschema.validator.android.internal;

import java8.util.Optional;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexFormatValidator extends AFormatValidator {

    @Override public Optional<String> validate(String subject) {
        try {
            Pattern.compile(subject);
        } catch (PatternSyntaxException e) {
            return Optional.of(String.format("[%s] is not a valid regular expression", subject));
        }
        return Optional.empty();
    }

    @Override public String formatName() {
        return "regex";
    }
}
