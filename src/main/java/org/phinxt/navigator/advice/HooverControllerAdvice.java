package org.phinxt.navigator.advice;

import org.phinxt.navigator.dto.ErrorDetail;
import org.phinxt.navigator.dto.ErrorResponse;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException validException) {
        List<ErrorDetail> details = new ArrayList<>();
        for (FieldError error : validException.getFieldErrors()) {
            details.add(new ErrorDetail(error.getField(), error.getDefaultMessage()));
        }

        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        AppConstants.ERROR_VALIDATION_FAILED,
                        details
                );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
