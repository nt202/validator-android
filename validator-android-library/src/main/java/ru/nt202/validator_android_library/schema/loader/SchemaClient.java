package ru.nt202.validator_android_library.schema.loader;

import java.io.InputStream;

/**
 * This interface is used by {@link SchemaLoader} to fetch the contents denoted by remote JSON
 * pointer.
 * <p>
 * Implementations are expected to support the HTTP/1.1 protocol, the support of other protocols is
 * optional.
 */
public abstract class SchemaClient  {

    public InputStream apply(final String url) {
        return get(url);
    }

    /**
     * Returns a stream to be used for reading the remote content (response body) of the URL. In the
     * case of a HTTP URL, implementations are expected send HTTP GET requests and the response is
     * expected to be represented in UTF-8 character set.
     *
     * @param url
     *         the URL of the remote resource
     * @return the input stream of the response
     * @throws ru.nt202.validator_android_library.schema.combatibility.UncheckedIOException
     *         if an IO error occurs.
     */
    public abstract InputStream get(String url);

}
