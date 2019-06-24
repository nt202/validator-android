package ru.nt202.validator.everit.json.schema.internal;

import static java.lang.String.format;

import java8.util.Optional;

import com.damnhandy.uri.template.MalformedUriTemplateException;
import com.damnhandy.uri.template.UriTemplate;

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
