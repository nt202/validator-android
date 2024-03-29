package ru.nt202.jsonschema.validator.android;

import java8.util.Objects;
import ru.nt202.jsonschema.validator.android.internal.JSONPrinter;

import static java8.util.Objects.requireNonNull;

/**
 * This class is used by {@link ru.nt202.jsonschema.validator.android.loader.SchemaLoader} to resolve JSON pointers
 * during the construction of the schema. This class has been made mutable to permit the loading of
 * recursive schemas.
 */
public class ReferenceSchema extends Schema {

    /**
     * Builder class for {@link ReferenceSchema}.
     */
    public static class Builder extends Schema.Builder<ReferenceSchema> {

        private ReferenceSchema retval;

        /**
         * The value of {@code "$ref"}
         */
        private String refValue = "";

        /**
         * This method caches its result, so multiple invocations will return referentially the same
         * {@link ReferenceSchema} instance.
         */
        @Override
        public ReferenceSchema build() {
            if (retval == null) {
                retval = new ReferenceSchema(this);
            }
            return retval;
        }

        public Builder refValue(String refValue) {
            this.refValue = refValue;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Schema referredSchema;

    private final String refValue;

    public ReferenceSchema(final Builder builder) {
        super(builder);
        this.refValue = requireNonNull(builder.refValue, "refValue cannot be null");
    }

    @Override
    public boolean definesProperty(String field) {
        if (referredSchema == null) {
            throw new IllegalStateException("referredSchema must be injected before validation");
        }
        return referredSchema.definesProperty(field);
    }

    public Schema getReferredSchema() {
        return referredSchema;
    }

    /**
     * Called by {@link ru.nt202.jsonschema.validator.android.loader.SchemaLoader#load()} to set the referred root
     * schema after completing the loading process of the entire schema document.
     *
     * @param referredSchema
     *         the referred schema
     */
    public void setReferredSchema(final Schema referredSchema) {
        if (this.referredSchema != null) {
            throw new IllegalStateException("referredSchema can be injected only once");
        }
        this.referredSchema = referredSchema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof ReferenceSchema) {
            ReferenceSchema that = (ReferenceSchema) o;
            return that.canEqual(this) &&
                    Objects.equals(refValue, that.refValue) &&
                    Objects.equals(referredSchema, that.referredSchema) &&
                    super.equals(that);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referredSchema, refValue);
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof ReferenceSchema;
    }

    @Override void accept(Visitor visitor) {
        visitor.visitReferenceSchema(this);
    }

    @Override void describePropertiesTo(JSONPrinter writer) {
        writer.key("$ref");
        writer.value(refValue);
    }
}
