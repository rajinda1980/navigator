package org.phinxt.navigator.advice;

import org.phinxt.navigator.dto.ValidationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidatorException extends RuntimeException {
    List<ValidationError> errors;

    public ValidatorException(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getMessages() {
        return errors;
    }
}
