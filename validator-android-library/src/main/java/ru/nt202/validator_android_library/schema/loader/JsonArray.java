package ru.nt202.validator_android_library.schema.loader;

import java8.util.function.Function;

import java.util.ArrayList;
import java.util.List;

import static java8.util.Objects.requireNonNull;

/**
 * @author erosb
 */
final class JsonArray extends JsonValue {

    private List<Object> storage;

    JsonArray(List<Object> storage) {
        super(storage);
        this.storage = requireNonNull(storage, "storage cannot be null");
    }

    public void forEach(JsonArrayIterator iterator) {
        for (int i = 0; i < storage.size(); ++i) {
            JsonValue childValue = at(i);
            iterator.apply(i, childValue);
        }
    }

    protected JsonValue at(int i) {
        return ls.childFor(i);
    }

    public int length() {
        return storage.size();
    }

    @Override public <R> R requireArray(Function<JsonArray, R> mapper) {
        return mapper.apply(this);
    }

    @Override
    protected Class<?> typeOfValue() {
        return JsonArray.class;
    }

    @Override protected Object value() {
        return this;
    }

    protected Object unwrap() {
        return new ArrayList<>(storage);
    }
}
