package ru.nt202.jsonschema.validator.android;

public class ReadWriteValidators {
    static ReadWriteValidator createForContext(ReadWriteContext context, ValidationFailureReporter failureReporter) {
        return context == null ? ReadWriteValidator.NONE :
                context == ReadWriteContext.READ ? new WriteOnlyValidator(failureReporter) :
                        new ReadOnlyValidator(failureReporter);
    }
}
