package ru.nt202.validator_android_library.schema.internal;

import com.damnhandy.uri.template.MalformedUriTemplateException;
import com.damnhandy.uri.template.UriTemplate;
import java8.util.Optional;

import static java.lang.String.format;

public class URITemplateFormatValidator extends AFormatValidator {

    @Override public Optional<String> validate(String subject) {
        try {
            UriTemplate.fromTemplate(subject);
            return Optional.empty();
        } catch (MalformedUriTemplateException e) {
            return Optional.of(format("[%s] is not a valid URI template", subject));
        }
    }

    @Override public String formatName() {
        return "uri-template";
    }
}
