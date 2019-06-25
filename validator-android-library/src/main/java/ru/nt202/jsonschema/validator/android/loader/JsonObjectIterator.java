package ru.nt202.jsonschema.validator.android.loader;

/**
 * @author erosb
 */
@FunctionalInterface
interface JsonObjectIterator {

    void apply(String key, JsonValue value);

}
