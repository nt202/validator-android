package ru.nt202.jsonschema.validator.android.internal;

import java8.util.Optional;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.temporal.ChronoField;

import static java8.util.Objects.requireNonNull;

/**
 * Base class for date and time format validators
 */
public class TemporalFormatValidator extends AFormatValidator {
    final static DateTimeFormatter SECONDS_FRACTION_FORMATTER = new DateTimeFormatterBuilder()
            .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
            .toFormatter();
    static final String ZONE_OFFSET_PATTERN = "XXX";

    private final DateTimeFormatter formatter;
    private final String formatsAccepted;

    TemporalFormatValidator(DateTimeFormatter formatter, String formatsAccepted) {
        this.formatter = requireNonNull(formatter, "formatter cannot be null");
        this.formatsAccepted = formatsAccepted;
    }

    @Override
    public Optional<String> validate(final String subject) {
        try {
            formatter.parse(subject);
            return Optional.empty();
        } catch (DateTimeParseException e) {
            return Optional.of(String.format("[%s] is not a valid %s. Expected %s", subject, formatName(), formatsAccepted));
        }
    }
}
