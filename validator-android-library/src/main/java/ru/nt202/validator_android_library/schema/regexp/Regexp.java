package ru.nt202.validator.everit.json.schema.regexp;

import static java8.util.Objects.requireNonNull;

import java8.util.Optional;

public interface Regexp {

    Optional<RegexpMatchingFailure> patternMatchingFailure(String input);

}

abstract class AbstractRegexp implements Regexp {

    private final String asString;

    AbstractRegexp(String asString) {
        this.asString = requireNonNull(asString, "asString cannot be null");
    }

    @Override public String toString() {
        return asString;
    }
}
