package ru.nt202.validator_android_library.schema.loader;

/**
 * @author erosb
 */
@FunctionalInterface
interface JsonArrayIterator {

    void apply(int index, JsonValue value);

}
