package ru.nt202.validator.everit.json.schema;

import static java8.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.ArrayList;

import com.annimon.stream.Stream;
import java8.util.Objects;
import java.util.List;
import java.util.Set;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import ru.nt202.validator.everit.json.schema.internal.JSONPrinter;
import ru.nt202.validator.json2.JSONArray;
import ru.nt202.validator.json2.JSONObject;

/**
 * Enum schema validator.
 */
public class EnumSchema extends Schema {

    static Object toJavaValue(Object orig) {
        if (orig instanceof JSONArray) {
            return ((JSONArray) orig).toList();
        } else if (orig instanceof JSONObject) {
            return ((JSONObject) orig).toMap();
        } else if (orig == JSONObject.NULL) {
            return null;
        } else {
            return orig;
        }
    }

    static List<Object> toJavaValues(List<Object> orgJsons) {
        return StreamSupport.stream(orgJsons).map(EnumSchema::toJavaValue).collect(toList());
    }

    /**
     * Builder class for {@link EnumSchema}.
     */
    public static class Builder extends Schema.Builder<EnumSchema> {

        private List<Object> possibleValues = new ArrayList<>();

        @Override
        public EnumSchema build() {
            return new EnumSchema(this);
        }

        public Builder possibleValue(Object possibleValue) {
            possibleValues.add(possibleValue);
            return this;
        }

        public Builder possibleValues(List<Object> possibleValues) {
            this.possibleValues = possibleValues;
            return this;
        }

        public Builder possibleValues(Set<Object> possibleValues) {
            this.possibleValues = StreamSupport.stream(possibleValues).collect(toList());
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final List<Object> possibleValues;

    public EnumSchema(Builder builder) {
        super(builder);
        possibleValues = Collections.unmodifiableList(toJavaValues(builder.possibleValues));
    }

    public Set<Object> getPossibleValues() {
        return StreamSupport.stream(possibleValues).collect(Collectors.toSet());
    }

    public List<Object> getPossibleValuesAsList() {
        return possibleValues;
    }

    @Override
    void describePropertiesTo(JSONPrinter writer) {
        writer.key("enum");
        writer.array();
        Stream.of(possibleValues).forEach(writer::value);
        writer.endArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof EnumSchema) {
            EnumSchema that = (EnumSchema) o;
            return that.canEqual(this) &&
                    Objects.equals(possibleValues, that.possibleValues) &&
                    super.equals(that);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), possibleValues);
    }

    @Override public void accept(Visitor visitor) {
        visitor.visitEnumSchema(this);
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof EnumSchema;
    }

}
