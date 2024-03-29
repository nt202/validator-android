package ru.nt202.jsonschema.validator.android;

import ru.nt202.jsonschema.validator.android.internal.JSONPrinter;

import static ru.nt202.jsonschema.validator.android.EnumSchema.toJavaValue;

public class ConstSchema extends Schema {

    public static class ConstSchemaBuilder extends Builder<ConstSchema> {

        private Object permittedValue;

        public ConstSchemaBuilder permittedValue(Object permittedValue) {
            this.permittedValue = permittedValue;
            return this;
        }

        @Override public ConstSchema build() {
            return new ConstSchema(this);
        }
    }

    public static ConstSchemaBuilder builder() {
        return new ConstSchemaBuilder();
    }

    private final Object permittedValue;

    protected ConstSchema(ConstSchemaBuilder builder) {
        super(builder);
        this.permittedValue = toJavaValue(builder.permittedValue);
    }

    @Override void describePropertiesTo(JSONPrinter writer) {
        writer.key("const");
        writer.value(this.permittedValue);
    }

    @Override void accept(Visitor visitor) {
        visitor.visitConstSchema(this);
    }

    public Object getPermittedValue() {
        return permittedValue;
    }
}
