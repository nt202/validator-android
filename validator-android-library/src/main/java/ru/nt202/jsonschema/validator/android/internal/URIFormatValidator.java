package ru.nt202.jsonschema.validator.android.internal;

import java8.util.Optional;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Implementation of the "uri" format value.
 */
public class URIFormatValidator extends AFormatValidator {

    private final boolean protocolRelativeURIPermitted;

    public URIFormatValidator() {
        this(true);
    }

    public URIFormatValidator(boolean protocolRelativeURIPermitted) {
        this.protocolRelativeURIPermitted = protocolRelativeURIPermitted;
    }

    @Override
    public Optional<String> validate(final String subject) {
        try {
            if (subject != null) {
                URI uri = new URI(subject);
                if (hasProtocol(uri) || (protocolRelativeURIPermitted && isProtocolRelativeURI(subject)))
                    return Optional.empty();
            }
        } catch (URISyntaxException e) {
            // Nothing To Do
        }
        return Optional.of(String.format("[%s] is not a valid URI", subject));
    }

    /**
     * @deprecated use {@code Optional.of(String.format("[%s] is not a valid URI", subject))} instead
     */
    @Deprecated
    protected Optional<String> failure(String subject) {
        return Optional.of(String.format("[%s] is not a valid URI", subject));
    }

    private boolean isProtocolRelativeURI(String subject) {
        return subject.startsWith("//");
    }

    private boolean hasProtocol(URI uri) {
        return uri.getScheme() != null;
    }

    @Override public String formatName() {
        return "uri";
    }
}
