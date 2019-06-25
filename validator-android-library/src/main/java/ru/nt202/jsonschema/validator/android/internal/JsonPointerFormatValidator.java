package ru.nt202.jsonschema.validator.android.internal;

import java8.util.Optional;
import ru.nt202.json2.JSONPointer;

import static java.lang.String.format;

public class JsonPointerFormatValidator extends AFormatValidator {

    @Override public Optional<String> validate(String subject) {
        if ("".equals(subject)) {
            return Optional.empty();
        }
        try {
            new JSONPointer(subject);
            if (subject.startsWith("#")) {
                return failure(subject);
            }
            return checkEscaping(subject);
        } catch (IllegalArgumentException e) {
            return failure(subject);
        }
    }

    protected Optional<String> failure(String subject) {
        return Optional.of(format("[%s] is not a valid JSON pointer", subject));
    }

    protected Optional<String> checkEscaping(String subject) {
        for (int i = 0; i < subject.length() - 1; ++i) {
            char c = subject.charAt(i);
            if (c == '~') {
                char next = subject.charAt(i + 1);
                if (next == '1' || next == '0') {
                    continue;
                }
                return failure(subject);
            }
        }
        if (subject.charAt(subject.length() - 1) == '~') {
            return failure(subject);
        }
        return Optional.empty();
    }

    @Override public String formatName() {
        return "json-pointer";
    }
}
