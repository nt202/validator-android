package ru.nt202.validator_android_library.schema.loader;

/**
 * @author erosb
 */
@FunctionalInterface
interface JsonObjectIterator {

    void apply(String key, JsonValue value);

}
