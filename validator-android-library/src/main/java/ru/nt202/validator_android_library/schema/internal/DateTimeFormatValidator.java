package ru.nt202.validator_android_library.schema.internal;

import java8.util.Optional;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import java.util.Arrays;

import static ru.nt202.validator_android_library.schema.internal.TemporalFormatValidator.SECONDS_FRACTION_FORMATTER;

/**
 * Implementation of the "date-time" format value.
 */
public class DateTimeFormatValidator extends AFormatValidator {

    private static class Delegate extends TemporalFormatValidator {

        Delegate() {
            super(FORMATTER, FORMATS_ACCEPTED);
        }

        @Override public String formatName() {
            return "date-time";
        }
    }

    private static final String FORMATS_ACCEPTED = Arrays.asList(
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,9}Z",
            "yyyy-MM-dd'T'HH:mm:ss[+-]HH:mm",
            "yyyy-MM-dd'T'HH:mm:ss.[0-9]{1,9}[+-]HH:mm"
    ).toString();

    private static final String PARTIAL_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern(PARTIAL_DATETIME_PATTERN)
            .appendOptional(SECONDS_FRACTION_FORMATTER)
            .appendPattern(TemporalFormatValidator.ZONE_OFFSET_PATTERN)
            .toFormatter();

    private Delegate delegate = new Delegate();

    @Override public Optional<String> validate(String subject) {
        return delegate.validate(subject);
    }

    @Override
    public String formatName() {
        return delegate.formatName();
    }
}
