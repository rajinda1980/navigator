package org.phinxt.navigator.utils;

import org.phinxt.navigator.advice.ValidatorException;
import org.phinxt.navigator.dto.HooverRequest;
import org.phinxt.navigator.dto.ValidationError;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AppValidator {

    public void validatePayload(HooverRequest request) throws ValidatorException {
        List<ValidationError> errors = new ArrayList<>();

        if (request.getRoomSize().stream().anyMatch(n -> n < 0)) {
            errors.add(new ValidationError(AppConstants.FIELD_NAME_ROOM_SIZE, AppConstants.NEGATIVE_ROOM_SIZE));
        }
        if (request.getRoomSize().stream().anyMatch( n -> n == 0)) {
            errors.add(new ValidationError(AppConstants.FIELD_NAME_ROOM_SIZE, AppConstants.ZERO_ROOM_SIZE));
        }
        if (request.getCoords().stream().anyMatch(n -> n < 0)) {
            errors.add(new ValidationError(AppConstants.FIELD_NAME_COORDS, AppConstants.NEGATIVE_COORDS));
        }
        if (null != request.getPatches() && !request.getPatches().isEmpty()
                && request.getPatches().stream().flatMap(Collection::stream).anyMatch(n -> n < 0)) {
            errors.add(new ValidationError(AppConstants.FIELD_NAME_PATCHES, AppConstants.NEGATIVE_PATCHES));
        }
        if (null != request.getInstructions() && !request.getInstructions().isBlank()) {
            String regex = ".*[^NEWS].*";
            if (Pattern.matches(regex, request.getInstructions())) {
                errors.add(new ValidationError(AppConstants.FIELD_NAME_INSTRUCTIONS, AppConstants.INVALID_INSTRUCTION_PATTERN));
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidatorException(errors);
        }
    }
}
