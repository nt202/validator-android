package ru.nt202.validator_android_library.schema.regexp;

import com.google.re2j.Pattern;
import java8.util.Optional;

class RE2JRegexp extends AbstractRegexp {

    private Pattern pattern;

    RE2JRegexp(String pattern) {
        super(pattern);
        this.pattern = Pattern.compile(pattern);
    }

    @Override public Optional<RegexpMatchingFailure> patternMatchingFailure(String input) {
        if (pattern.matcher(input).find()) {
            return Optional.empty();
        } else {
            return Optional.of(new RegexpMatchingFailure());
        }
    }

}

public class RE2JRegexpFactory implements RegexpFactory {
    @Override public Regexp createHandler(String input) {
        return new RE2JRegexp(input);
    }
}
