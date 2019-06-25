package ru.nt202.jsonschema.validator.android.loader;

import ru.nt202.jsonschema.validator.android.FormatValidator;
import ru.nt202.jsonschema.validator.android.loader.internal.DefaultSchemaClient;
import ru.nt202.jsonschema.validator.android.regexp.JavaUtilRegexpFactory;
import ru.nt202.jsonschema.validator.android.regexp.RegexpFactory;

import java.util.Map;

import static java8.util.Objects.requireNonNull;
import static ru.nt202.jsonschema.validator.android.loader.SpecificationVersion.DRAFT_4;

/**
 * @author erosb
 */
class LoaderConfig {

    static LoaderConfig defaultV4Config() {
        return new LoaderConfig(new DefaultSchemaClient(), DRAFT_4.defaultFormatValidators(), DRAFT_4, false);
    }

    final SchemaClient httpClient;

    final Map<String, FormatValidator> formatValidators;

    final SpecificationVersion specVersion;

    final boolean useDefaults;

    final boolean nullableSupport;

    final RegexpFactory regexpFactory;

    LoaderConfig(SchemaClient httpClient, Map<String, FormatValidator> formatValidators,
            SpecificationVersion specVersion, boolean useDefaults) {
        this(httpClient, formatValidators, specVersion, useDefaults, false, new JavaUtilRegexpFactory());
    }

    LoaderConfig(SchemaClient httpClient, Map<String, FormatValidator> formatValidators,
            SpecificationVersion specVersion, boolean useDefaults, boolean nullableSupport,
            RegexpFactory regexpFactory) {
        this.httpClient = requireNonNull(httpClient, "httpClient cannot be null");
        this.formatValidators = requireNonNull(formatValidators, "formatValidators cannot be null");
        this.specVersion = requireNonNull(specVersion, "specVersion cannot be null");
        this.useDefaults = useDefaults;
        this.nullableSupport = nullableSupport;
        this.regexpFactory = requireNonNull(regexpFactory, "regexpFactory cannot be null");
    }

}
