package ru.nt202.validator_android_library.schema.loader;

import ru.nt202.validator_android_library.schema.FormatValidator;
import ru.nt202.validator_android_library.schema.StringSchema;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static java8.util.Objects.requireNonNull;
import static ru.nt202.validator_android_library.schema.loader.SpecificationVersion.DRAFT_4;

/**
 * @author erosb
 */
public class StringSchemaLoader {

    private LoadingState ls;

    private Map<String, FormatValidator> formatValidators;

    private boolean useDefault;

    /**
     * Creates an instance with {@link SpecificationVersion#defaultFormatValidators()}  draft v4 format validators}.
     *
     * @deprecated explicitly specify the format validators with {@link #StringSchemaLoader(LoadingState, Map)} instead
     */
    @Deprecated
    public StringSchemaLoader(LoadingState ls) {
        this(ls, DRAFT_4.defaultFormatValidators());
    }

    StringSchemaLoader(LoadingState ls, Map<String, FormatValidator> formatValidators) {
        this.ls = requireNonNull(ls, "ls cannot be null");
        this.formatValidators = unmodifiableMap(requireNonNull(formatValidators, "formatValidators cannot be null"));
    }

    public StringSchema.Builder load() {
        StringSchema.Builder builder = StringSchema.builder();
        ls.schemaJson().maybe("minLength").map(JsonValue::requireInteger).ifPresent(builder::minLength);
        ls.schemaJson().maybe("maxLength").map(JsonValue::requireInteger).ifPresent(builder::maxLength);
        ls.schemaJson().maybe("pattern").map(JsonValue::requireString)
                .map(ls.config.regexpFactory::createHandler)
                .ifPresent(builder::pattern);
        ls.schemaJson().maybe("format").map(JsonValue::requireString)
                .ifPresent(format -> addFormatValidator(builder, format));
        return builder;
    }

    private void addFormatValidator(StringSchema.Builder builder, String formatName) {
        FormatValidator formatValidator = formatValidators.get(formatName);
        if (formatValidator != null) {
            builder.formatValidator(formatValidator);
        }
    }

}
