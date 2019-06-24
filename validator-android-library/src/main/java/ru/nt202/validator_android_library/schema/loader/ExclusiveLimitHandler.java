package ru.nt202.validator_android_library.schema.loader;

import ru.nt202.validator_android_library.schema.NumberSchema;

class V4ExclusiveLimitHandler implements  ExclusiveLimitHandler {

    @Override
    public void handleExclusiveMinimum(JsonValue exclMinimum, NumberSchema.Builder schemaBuilder) {
        schemaBuilder.exclusiveMinimum(exclMinimum.requireBoolean());
    }

    @Override
    public void handleExclusiveMaximum(JsonValue exclMaximum, NumberSchema.Builder schemaBuilder) {
        schemaBuilder.exclusiveMaximum(exclMaximum.requireBoolean());
    }
}

class V6ExclusiveLimitHandler implements ExclusiveLimitHandler {

    @Override
    public void handleExclusiveMinimum(JsonValue exclMinimum, NumberSchema.Builder schemaBuilder) {
        schemaBuilder.exclusiveMinimum(exclMinimum.requireNumber());
    }

    @Override
    public void handleExclusiveMaximum(JsonValue exclMaximum, NumberSchema.Builder schemaBuilder) {
        schemaBuilder.exclusiveMaximum(exclMaximum.requireNumber());
    }

}

interface ExclusiveLimitHandler {

    void handleExclusiveMinimum(JsonValue exclMinimum, NumberSchema.Builder schemaBuilder);

    void handleExclusiveMaximum(JsonValue exclMaximum, NumberSchema.Builder schemaBuilder);

}
