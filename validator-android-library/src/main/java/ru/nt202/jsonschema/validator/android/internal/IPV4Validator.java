package ru.nt202.jsonschema.validator.android.internal;

import java8.util.Optional;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Implementation of the "ipv4" format value.
 */
public class IPV4Validator extends IPAddressValidator {

    @Override
    public Optional<String> validate(final String subject) {
        return InetAddressValidator.getInstance().isValidInet4Address(subject) ?
                Optional.empty() :
                Optional.of(String.format("[%s] is not a valid ipv4 address", subject));
    }

    @Override
    public String formatName() {
        return "ipv4";
    }
}
