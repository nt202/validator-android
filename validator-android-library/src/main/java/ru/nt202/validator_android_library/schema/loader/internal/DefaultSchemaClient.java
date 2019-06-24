package ru.nt202.validator_android_library.schema.loader.internal;

import ru.nt202.validator_android_library.schema.combatibility.UncheckedIOException;
import ru.nt202.validator_android_library.schema.loader.SchemaClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A {@link SchemaClient} implementation which uses {@link URL} for reading the remote content.
 */
public class DefaultSchemaClient extends SchemaClient {

    public InputStream get(final String url) {
        try {
            return (InputStream) new URL(url).getContent();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
