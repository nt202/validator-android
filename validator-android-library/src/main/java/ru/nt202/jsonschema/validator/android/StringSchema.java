package ru.nt202.jsonschema.validator.android;

import java8.util.Objects;
import ru.nt202.jsonschema.validator.android.combatibility.FormatValidators;
import ru.nt202.jsonschema.validator.android.internal.AFormatValidator;
import ru.nt202.jsonschema.validator.android.internal.JSONPrinter;
import ru.nt202.jsonschema.validator.android.regexp.JavaUtilRegexpFactory;
import ru.nt202.jsonschema.validator.android.regexp.Regexp;

import static java8.util.Objects.requireNonNull;
import static ru.nt202.jsonschema.validator.android.FormatValidator.NONE;

/**
 * {@code String} schema validator.
 */
public class StringSchema extends Schema {

    /**
     * Builder class for {@link StringSchema}.
     */
    public static class Builder extends Schema.Builder<StringSchema> {

        private Integer minLength;

        private Integer maxLength;

        private Regexp pattern;

        private boolean requiresString = true;

        private FormatValidator formatValidator = NONE;

        @Override
        public StringSchema build() {
            return new StringSchema(this);
        }

        /**
         * Setter for the format validator. It should be used in conjunction with
         * {@link FormatValidators#forFormat(String)} if a {@code "format"} value is found in a schema
         * json.
         *
         * @param formatValidator
         *         the format validator
         * @return {@code this}
         */
        public Builder formatValidator(final FormatValidator formatValidator) {
            this.formatValidator = requireNonNull(formatValidator, "formatValidator cannot be null");
            return this;
        }

        public Builder maxLength(final Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Builder minLength(final Integer minLength) {
            this.minLength = minLength;
            return this;
        }

        public Builder pattern(final String pattern) {
            return pattern(new JavaUtilRegexpFactory().createHandler(pattern));
        }

        public Builder pattern(Regexp pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder requiresString(final boolean requiresString) {
            this.requiresString = requiresString;
            return this;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private final Integer minLength;

    private final Integer maxLength;

    private final Regexp pattern;

    private final boolean requiresString;

    private final FormatValidator formatValidator;

    public StringSchema() {
        this(builder());
    }

    /**
     * Constructor.
     *
     * @param builder
     *         the builder object containing validation criteria
     */
    public StringSchema(final Builder builder) {
        super(builder);
        this.minLength = builder.minLength;
        this.maxLength = builder.maxLength;
        this.requiresString = builder.requiresString;
        this.pattern = builder.pattern;
        this.formatValidator = builder.formatValidator;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    Regexp getRegexpPattern() {
        return pattern;
    }

    public java.util.regex.Pattern getPattern() {
        if (pattern == null) {
            return null;
        } else {
            return java.util.regex.Pattern.compile(pattern.toString());
        }
    }

    @Override void accept(Visitor visitor) {
        visitor.visitStringSchema(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof StringSchema) {
            StringSchema that = (StringSchema) o;
            return that.canEqual(this) &&
                    requiresString == that.requiresString &&
                    Objects.equals(minLength, that.minLength) &&
                    Objects.equals(maxLength, that.maxLength) &&
                    Objects.equals(pattern, that.pattern) &&
                    Objects.equals(formatValidator, that.formatValidator) &&
                    super.equals(that);
        } else {
            return false;
        }
    }

    public FormatValidator getFormatValidator() {
        return formatValidator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minLength, maxLength, pattern, requiresString, formatValidator);
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof StringSchema;
    }

    @Override
    void describePropertiesTo(JSONPrinter writer) {
        if (requiresString) {
            writer.key("type").value("string");
        }
        writer.ifPresent("minLength", minLength);
        writer.ifPresent("maxLength", maxLength);
        writer.ifPresent("pattern", pattern);
        if (formatValidator != null && !NONE.equals(formatValidator)) {
            writer.key("format").value(((AFormatValidator)formatValidator).formatName());
        }
    }

    public boolean requireString() {
        return requiresString;
    }
}
