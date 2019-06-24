package ru.nt202.validator_android_library.schema.loader.internal;

import java8.util.function.Consumer;

import java.net.URI;

/**
 * Event handler interface used by {@link TypeBasedMultiplexer} to notify client(s) (which is
 * currently a schema loader instance) about resolution scope changes.
 */
@FunctionalInterface
public interface ResolutionScopeChangeListener extends Consumer<URI> {

    @Override
    default void accept(final URI t) {
        resolutionScopeChanged(t);
    }

    void resolutionScopeChanged(URI newResolutionScope);
}
