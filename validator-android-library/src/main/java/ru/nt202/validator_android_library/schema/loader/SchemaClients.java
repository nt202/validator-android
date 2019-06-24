package ru.nt202.validator_android_library.schema.loader;

import java.io.InputStream;

public class SchemaClients {

    static <T extends SchemaClient> InputStream apply(T client, final String url) {
        return client.get(url);
    }
}
