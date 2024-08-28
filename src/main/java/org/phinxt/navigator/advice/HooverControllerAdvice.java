package org.phinxt.navigator.advice;

import org.phinxt.navigator.dto.ErrorDetail;
import org.phinxt.navigator.dto.ErrorResponse;
import org.phinxt.navigator.dto.ValidationError;
import org.phinxt.navigator.utils.AppConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HooverControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException validException) {
        List<ErrorDetail> details = new ArrayList<>();
        for (FieldError error : validException.getFieldErrors()) {
            details.add(new ErrorDetail(error.getField(), error.getDefaultMessage()));
        }
        ErrorResponse response = getErrorResponse(HttpStatus.BAD_REQUEST, AppConstants.ERROR_VALIDATION_FAILED, details);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        List<ErrorDetail> details = new ArrayList<>();
        ErrorResponse response = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), details);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ValidatorException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidatorException exception) {
        List<ErrorDetail> details = new ArrayList<>();
        for (ValidationError error : exception.getMessages()) {
            details.add(new ErrorDetail(error.getFieldName(), error.getMessage()));
        }
        ErrorResponse response = getErrorResponse(HttpStatus.BAD_REQUEST, AppConstants.ERROR_VALIDATION_FAILED, details);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse getErrorResponse(HttpStatus badRequest, String errorValidationFailed, List<ErrorDetail> details) {
        return
                new ErrorResponse(
                        LocalDateTime.now(),
                        badRequest.value(),
                        badRequest.getReasonPhrase(),
                        errorValidationFailed,
                        details
                );
    }
}
