package ru.nt202.jsonschema.validator.android.loader;

import java8.util.Optional;
import java8.util.stream.StreamSupport;
import ru.nt202.jsonschema.validator.android.*;

import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java8.util.Objects.requireNonNull;
import static ru.nt202.jsonschema.validator.android.loader.SpecificationVersion.DRAFT_4;
import static ru.nt202.jsonschema.validator.android.loader.SpecificationVersion.DRAFT_7;

class ExtractionResult {

    final Set<String> consumedKeys;

    final Collection<Schema.Builder<?>> extractedSchemas;

    ExtractionResult(Set<String> consumedKeys, Collection<Schema.Builder<?>> extractedSchemas) {
        this.consumedKeys = requireNonNull(consumedKeys, "consumedKeys cannot be null");
        this.extractedSchemas = requireNonNull(extractedSchemas, "extractedSchemas cannot be null");
    }

    ExtractionResult(String consumedKeys, Collection<Schema.Builder<?>> extactedSchemas) {
        this(singleton(consumedKeys), extactedSchemas);
    }

}

interface SchemaExtractor {

    ExtractionResult extract(JsonObject schemaJson);

}

abstract class AbstractSchemaExtractor implements SchemaExtractor {

    static final List<String> NUMBER_SCHEMA_PROPS = asList("minimum", "maximum",
            "exclusiveMinimum", "exclusiveMaximum", "multipleOf");

    static final List<String> STRING_SCHEMA_PROPS = asList("minLength", "maxLength",
            "pattern", "format");

    protected JsonObject schemaJson;

    private Set<String> consumedKeys;

    final SchemaLoader defaultLoader;

    private ExclusiveLimitHandler exclusiveLimitHandler;

    AbstractSchemaExtractor(SchemaLoader defaultLoader) {
        this.defaultLoader = requireNonNull(defaultLoader, "defaultLoader cannot be null");
    }

    @Override
    public final ExtractionResult extract(JsonObject schemaJson) {
        this.schemaJson = requireNonNull(schemaJson, "schemaJson cannot be null");
        this.exclusiveLimitHandler = ExclusiveLimitHandlers.ofSpecVersion(config().specVersion);
        consumedKeys = new HashSet<>(schemaJson.keySet().size());
        return new ExtractionResult(consumedKeys, extract());
    }

    void keyConsumed(String key) {
        if (schemaJson.keySet().contains(key)) {
            consumedKeys.add(key);
        }
    }

    void keysConsumed(List<String> keys) {
        for(String key : keys) {
            keyConsumed(key);
        }
    }

    JsonValue require(String key) {
        keyConsumed(key);
        return schemaJson.require(key);
    }

    Optional<JsonValue> maybe(String key) {
        keyConsumed(key);
        return schemaJson.maybe(key);
    }

    boolean containsKey(String key) {
        return schemaJson.containsKey(key);
    }

    boolean schemaHasAnyOf(Collection<String> propNames) {
        return StreamSupport.stream(propNames).anyMatch(schemaJson::containsKey);
    }

    LoaderConfig config() {
        return schemaJson.ls.config;
    }

    ObjectSchema.Builder buildObjectSchema() {
        keysConsumed(config().specVersion.objectKeywords());
        return new ObjectSchemaLoader(schemaJson.ls, config(), defaultLoader).load();
    }

    ArraySchema.Builder buildArraySchema() {
        keysConsumed(config().specVersion.arrayKeywords());
        return new ArraySchemaLoader(schemaJson.ls, config(), defaultLoader).load();
    }

    NumberSchema.Builder buildNumberSchema() {
        keysConsumed(PropertySnifferSchemaExtractor.NUMBER_SCHEMA_PROPS);
        NumberSchema.Builder builder = NumberSchema.builder();
        maybe("minimum").map(JsonValue::requireNumber).ifPresent(builder::minimum);
        maybe("maximum").map(JsonValue::requireNumber).ifPresent(builder::maximum);
        maybe("multipleOf").map(JsonValue::requireNumber).ifPresent(builder::multipleOf);
        maybe("exclusiveMinimum").ifPresent(exclMin -> exclusiveLimitHandler.handleExclusiveMinimum(exclMin, builder));
        maybe("exclusiveMaximum").ifPresent(exclMax -> exclusiveLimitHandler.handleExclusiveMaximum(exclMax, builder));
        return builder;
    }

    abstract List<Schema.Builder<?>> extract();
}

class EnumSchemaExtractor extends AbstractSchemaExtractor {

    EnumSchemaExtractor(SchemaLoader defaultLoader) {
        super(defaultLoader);
    }

    @Override List<Schema.Builder<?>> extract() {
        if (!containsKey("enum")) {
            return emptyList();
        }
        EnumSchema.Builder builder = EnumSchema.builder();
        List<Object> possibleValues = new ArrayList<>();

        require("enum").requireArray().forEach((i, item) -> possibleValues.add(item.unwrap()));
        builder.possibleValues(possibleValues);
        return singletonList(builder);
    }

}

class ReferenceSchemaExtractor extends AbstractSchemaExtractor {

    ReferenceSchemaExtractor(SchemaLoader defaultLoader) {
        super(defaultLoader);
    }

    @Override List<Schema.Builder<?>> extract() {
        if (containsKey("$ref")) {
            String ref = require("$ref").requireString();
            return singletonList(new ReferenceLookup(schemaJson.ls).lookup(ref, schemaJson));
        }
        return emptyList();
    }
}

class PropertySnifferSchemaExtractor extends AbstractSchemaExtractor {

    static final List<String> CONDITIONAL_SCHEMA_KEYWORDS = asList("if", "then", "else");

    PropertySnifferSchemaExtractor(SchemaLoader defaultLoader) {
        super(defaultLoader);
    }

    @Override List<Schema.Builder<?>> extract() {
        List<Schema.Builder<?>> builders = new ArrayList<>(1);
        if (schemaHasAnyOf(config().specVersion.arrayKeywords())) {
            builders.add(new ArraySchemaLoader(schemaJson.ls, config(), defaultLoader).load().requiresArray(false));
        }
        if (schemaHasAnyOf(config().specVersion.objectKeywords())) {
            builders.add(new ObjectSchemaLoader(schemaJson.ls, config(), defaultLoader).load().requiresObject(false));
        }
        if (schemaHasAnyOf(NUMBER_SCHEMA_PROPS)) {
            builders.add(buildNumberSchema().requiresNumber(false));
        }
        if (schemaHasAnyOf(STRING_SCHEMA_PROPS)) {
            builders.add(new StringSchemaLoader(schemaJson.ls, config().formatValidators).load().requiresString(false));
        }
        if (config().specVersion.isAtLeast(DRAFT_7) && schemaHasAnyOf(CONDITIONAL_SCHEMA_KEYWORDS)) {
            builders.add(buildConditionalSchema());
        }
        return builders;
    }

    private ConditionalSchema.Builder buildConditionalSchema() {
        ConditionalSchema.Builder builder = ConditionalSchema.builder();
        maybe("if").map(defaultLoader::loadChild).map(Schema.Builder::build).ifPresent(builder::ifSchema);
        maybe("then").map(defaultLoader::loadChild).map(Schema.Builder::build).ifPresent(builder::thenSchema);
        maybe("else").map(defaultLoader::loadChild).map(Schema.Builder::build).ifPresent(builder::elseSchema);
        return builder;
    }

}

class TypeBasedSchemaExtractor extends AbstractSchemaExtractor {

    TypeBasedSchemaExtractor(SchemaLoader defaultLoader) {
        super(defaultLoader);
    }

    @Override List<Schema.Builder<?>> extract() {
        if (containsKey("type")) {
            return singletonList(require("type").canBeMappedTo(JsonArray.class, arr -> (Schema.Builder) buildAnyOfSchemaForMultipleTypes())
                    .orMappedTo(String.class, this::loadForExplicitType)
                    .requireAny());
        } else {
            return emptyList();
        }
    }

    private CombinedSchema.Builder buildAnyOfSchemaForMultipleTypes() {
        JsonArray subtypeJsons = require("type").requireArray();
        Collection<Schema> subschemas = new ArrayList<>(subtypeJsons.length());
        subtypeJsons.forEach((j, raw) -> {
            subschemas.add(loadForExplicitType(raw.requireString()).build());
        });
        return CombinedSchema.anyOf(subschemas);
    }

    private Schema.Builder<?> loadForExplicitType(String typeString) {
        switch (typeString) {
        case "string":
            keysConsumed(PropertySnifferSchemaExtractor.STRING_SCHEMA_PROPS);
            return new StringSchemaLoader(schemaJson.ls, config().formatValidators).load();
        case "integer":
            return buildNumberSchema().requiresInteger(true);
        case "number":
            return buildNumberSchema();
        case "boolean":
            return BooleanSchema.builder();
        case "null":
            return NullSchema.builder();
        case "array":
            return buildArraySchema();
        case "object":
            return buildObjectSchema();
        default:
            throw new SchemaException(schemaJson.ls.locationOfCurrentObj(), format("unknown type: [%s]", typeString));
        }
    }

}

class NotSchemaExtractor extends AbstractSchemaExtractor {

    NotSchemaExtractor(SchemaLoader defaultLoader) {
        super(defaultLoader);
    }

    @Override List<Schema.Builder<?>> extract() {
        if (containsKey("not")) {
            Schema mustNotMatch = defaultLoader.loadChild(require("not")).build();
            return singletonList(NotSchema.builder().mustNotMatch(mustNotMatch));
        }
        return emptyList();
    }
}

class ConstSchemaExtractor extends AbstractSchemaExtractor {

    ConstSchemaExtractor(SchemaLoader defaultLoader) {
        super(defaultLoader);
    }

    @Override List<Schema.Builder<?>> extract() {
        if (config().specVersion != DRAFT_4 && containsKey("const")) {
            return singletonList(ConstSchema.builder().permittedValue(require("const").unwrap()));
        } else {
            return emptyList();
        }
    }
}
