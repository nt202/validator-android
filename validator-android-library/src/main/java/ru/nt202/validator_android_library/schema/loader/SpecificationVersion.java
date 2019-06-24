package ru.nt202.validator.everit.json.schema.loader;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java8.util.stream.StreamSupport;
import ru.nt202.validator.everit.json.schema.FormatValidator;
import ru.nt202.validator.everit.json.schema.internal.*;

/**
 * @author erosb
 */
enum SpecificationVersion {

    DRAFT_4 {
        @Override List<String> arrayKeywords() {
            return V4_ARRAY_KEYWORDS;
        }

        @Override List<String> objectKeywords() {
            return V4_OBJECT_KEYWORDS;
        }

        @Override String idKeyword() {
            return "id";
        }

        @Override String metaSchemaUrl() {
            return "http://json-schema.org/draft-04/schema";
        }

        @Override Map<String, FormatValidator> defaultFormatValidators() {
            return V4_VALIDATORS;
        }

    }, DRAFT_6 {
        @Override List<String> arrayKeywords() {
            return V6_ARRAY_KEYWORDS;
        }

        @Override List<String> objectKeywords() {
            return V6_OBJECT_KEYWORDS;
        }

        @Override String idKeyword() {
            return "$id";
        }

        @Override String metaSchemaUrl() {
            return "http://json-schema.org/draft-06/schema";
        }

        @Override Map<String, FormatValidator> defaultFormatValidators() {
            return V6_VALIDATORS;
        }

    }, DRAFT_7 {
        @Override List<String> arrayKeywords() {
            return V6_ARRAY_KEYWORDS;
        }

        @Override List<String> objectKeywords() {
            return V6_OBJECT_KEYWORDS;
        }

        @Override String idKeyword() {
            return DRAFT_6.idKeyword();
        }

        @Override String metaSchemaUrl() {
            return "http://json-schema.org/draft-07/schema";
        }

        @Override Map<String, FormatValidator> defaultFormatValidators() {
            return V7_VALIDATORS;
        }
    };

    static SpecificationVersion getByMetaSchemaUrl(String metaSchemaUrl) {
        return StreamSupport.stream(Arrays.asList(values()))
                .filter(v -> metaSchemaUrl.startsWith(v.metaSchemaUrl()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        format("could not determine schema version: no meta-schema is known with URL [%s]", metaSchemaUrl)
                ));
    }

    private static final List<String> V6_OBJECT_KEYWORDS = keywords("properties", "required",
            "minProperties",
            "maxProperties",
            "dependencies",
            "patternProperties",
            "additionalProperties",
            "propertyNames");

    private static final List<String> V6_ARRAY_KEYWORDS = keywords("items", "additionalItems", "minItems",
            "maxItems", "uniqueItems", "contains");

    private static final List<String> V4_OBJECT_KEYWORDS = keywords("properties", "required",
            "minProperties",
            "maxProperties",
            "dependencies",
            "patternProperties",
            "additionalProperties");

    private static final List<String> V4_ARRAY_KEYWORDS = keywords("items", "additionalItems", "minItems",
            "maxItems", "uniqueItems");

    private static List<String> keywords(String... keywords) {
        return unmodifiableList(asList(keywords));
    }

    private static final Map<String, FormatValidator> V4_VALIDATORS = formatValidators(null,
            new DateTimeFormatValidator(),
            new URIV4FormatValidator(),
            new EmailFormatValidator(),
            new IPV4Validator(),
            new IPV6Validator(),
            new HostnameFormatValidator()
    );

    private static final Map<String, FormatValidator> V6_VALIDATORS = formatValidators(V4_VALIDATORS,
            new JsonPointerFormatValidator(),
            new URIFormatValidator(),
            new URIReferenceFormatValidator(),
            new URITemplateFormatValidator()
    );

    private static final Map<String, FormatValidator> V7_VALIDATORS = formatValidators(V6_VALIDATORS,
            new DateFormatValidator(),
            new URIFormatValidator(false),
            new TimeFormatValidator(),
            new RegexFormatValidator(),
            new RelativeJsonPointerFormatValidator()
    );

    private static Map<String, FormatValidator> formatValidators(Map<String, FormatValidator> parent, FormatValidator... validators) {
        Map<String, FormatValidator> validatorMap = (parent == null) ? new HashMap<>() : new HashMap<>(parent);
        for (FormatValidator validator : validators) {
            validatorMap.put(((AFormatValidator)validator).formatName(), validator);
        }
        return unmodifiableMap(validatorMap);
    }

    abstract List<String> arrayKeywords();

    abstract List<String> objectKeywords();

    abstract String idKeyword();

    abstract String metaSchemaUrl();

    abstract Map<String, FormatValidator> defaultFormatValidators();

    public boolean isAtLeast(SpecificationVersion lowerInclusiveBound) {
        return this.ordinal() >= lowerInclusiveBound.ordinal();
    }
}
