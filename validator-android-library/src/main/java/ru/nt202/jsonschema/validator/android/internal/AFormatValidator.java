package ru.nt202.jsonschema.validator.android.internal;

import ru.nt202.jsonschema.validator.android.FormatValidator;

public abstract class AFormatValidator implements FormatValidator {
    /**
     * Provides the name of this format.
     * <p>
     * Unless specified otherwise the {@link ru.nt202.jsonschema.validator.android.loader.SchemaLoader} will use this
     * name to recognize string schemas using this format.
     * </p>
     * The default implementation of this method returns {@code "unnamed-format"}. It is strongly
     * recommended for implementations to give a more meaningful name by overriding this method.
     *
     * @return the format name.
     */
    public String formatName() {
        return "unnamed-format";
    }
}
