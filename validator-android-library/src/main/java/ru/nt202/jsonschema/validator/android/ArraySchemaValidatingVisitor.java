package ru.nt202.jsonschema.validator.android;

import java8.util.Optional;
import java8.util.function.IntFunction;
import java8.util.stream.IntStream;
import java8.util.stream.IntStreams;
import ru.nt202.json2.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static java8.util.Objects.requireNonNull;

class ArraySchemaValidatingVisitor extends Visitor {

    private final Object subject;

    private final ValidatingVisitor owner;

    private JSONArray arraySubject;

    private ArraySchema arraySchema;

    private int subjectLength;

    public ArraySchemaValidatingVisitor(Object subject, ValidatingVisitor owner) {
        this.subject = subject;
        this.owner = requireNonNull(owner, "owner cannot be null");
    }

    @Override void visitArraySchema(ArraySchema arraySchema) {
        if (owner.passesTypeCheck(JSONArray.class, arraySchema.requiresArray(), arraySchema.isNullable())) {
            this.arraySubject = (JSONArray) subject;
            this.subjectLength = arraySubject.length();
            this.arraySchema = arraySchema;
            super.visitArraySchema(arraySchema);
        }
    }

    @Override void visitMinItems(Integer minItems) {
        if (minItems != null && subjectLength < minItems) {
            owner.failure("expected minimum item count: " + minItems + ", found: " + subjectLength, "minItems");
        }
    }

    @Override void visitMaxItems(Integer maxItems) {
        if (maxItems != null && maxItems < subjectLength) {
            owner.failure("expected maximum item count: " + maxItems + ", found: " + subjectLength, "maxItems");
        }
    }

    @Override void visitUniqueItems(boolean uniqueItems) {
        if (!uniqueItems || subjectLength == 0) {
            return;
        }
        Collection<Object> uniques = new ArrayList<Object>(subjectLength);
        for (int i = 0; i < subjectLength; ++i) {
            Object item = arraySubject.get(i);
            for (Object contained : uniques) {
                if (ObjectComparator.deepEquals(contained, item)) {
                    owner.failure("array items are not unique", "uniqueItems");
                    return;
                }
            }
            uniques.add(item);
        }
    }

    @Override void visitAllItemSchema(Schema allItemSchema) {
        if (allItemSchema != null) {
            validateItemsAgainstSchema(IntStreams.range(0, subjectLength), allItemSchema);
        }
    }

    @Override void visitItemSchema(int index, Schema itemSchema) {
        if (index >= subjectLength) {
            return;
        }
        Object subject = arraySubject.get(index);
        String idx = String.valueOf(index);
        ifFails(itemSchema, subject)
                .map(exc -> exc.prepend(idx))
                .ifPresent(owner::failure);
    }

    @Override void visitAdditionalItems(boolean additionalItems) {
        List<Schema> itemSchemas = arraySchema.getItemSchemas();
        int itemSchemaCount = itemSchemas == null ? 0 : itemSchemas.size();
        if (itemSchemas != null && !additionalItems && subjectLength > itemSchemaCount) {
            owner.failure(format("expected: [%d] array items, found: [%d]", itemSchemaCount, subjectLength), "items");
        }
    }

    @Override void visitSchemaOfAdditionalItems(Schema schemaOfAdditionalItems) {
        if (schemaOfAdditionalItems == null) {
            return;
        }
        int validationFrom = Math.min(subjectLength, arraySchema.getItemSchemas().size());
        validateItemsAgainstSchema(IntStreams.range(validationFrom, subjectLength), schemaOfAdditionalItems);
    }

    private void validateItemsAgainstSchema(IntStream indices, Schema schema) {
        validateItemsAgainstSchema(indices, i -> schema);
    }

    private void validateItemsAgainstSchema(IntStream indices, IntFunction<Schema> schemaForIndex) {
        for (int i : indices.toArray()) {
            String copyOfI = String.valueOf(i); // i is not effectively final so we copy it
            ifFails(schemaForIndex.apply(i), arraySubject.get(i))
                    .map(exc -> exc.prepend(copyOfI))
                    .ifPresent(owner::failure);
        }
    }

    private Optional<ValidationException> ifFails(Schema schema, Object input) {
        return Optional.ofNullable(owner.getFailureOfSchema(schema, input));
    }

    @Override void visitContainedItemSchema(Schema containedItemSchema) {
        if (containedItemSchema == null) {
            return;
        }
        for (int i = 0; i < arraySubject.length(); i++) {
            Optional<ValidationException> exception = ifFails(containedItemSchema, arraySubject.get(i));
            if (!exception.isPresent()) {
                return;
            }
        }
        owner.failure("expected at least one array item to match 'contains' schema", "contains");
    }
}
