package ru.nt202.validator_android_library.schema;

import ru.nt202.validator_android_library.schema.internal.JSONPrinter;

/**
 * @author erosb
 */
public class TrueSchema extends EmptySchema {

    public static class Builder extends EmptySchema.Builder {

        @Override public TrueSchema build() {
            return new TrueSchema(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public TrueSchema(Builder builder) {
        super(builder);
    }

    @Override
    public void describeTo(JSONPrinter writer) {
        writer.value(true);
    }

    @Override
    public String toString() {
        return "true";
    }
}
