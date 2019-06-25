package ru.nt202.jsonschema.validator.android.loader;

/**
 * @author erosb
 */
@FunctionalInterface
interface JsonArrayIterator {

    void apply(int index, JsonValue value);

}
