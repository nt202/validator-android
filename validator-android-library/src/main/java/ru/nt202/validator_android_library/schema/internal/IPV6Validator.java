package ru.nt202.validator_android_library.schema.internal;

import java8.util.Optional;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Implementation of the "ipv6" format value.
 */
public class IPV6Validator extends IPAddressValidator {

    @Override
    public Optional<String> validate(final String subject) {
        return (subject != null) && InetAddressValidator.getInstance().isValidInet6Address(subject) ?
                Optional.empty() :
                Optional.of(String.format("[%s] is not a valid ipv6 address", subject));
    }

    @Override
    public String formatName() {
        return "ipv6";
    }
}
