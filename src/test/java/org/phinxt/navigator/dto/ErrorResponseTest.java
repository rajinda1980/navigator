package org.phinxt.navigator.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.phinxt.navigator.utils.AppConstants;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponseTest {

    @DisplayName("Should return the correct error response")
    @Test
    void shouldReturnErrorResponse() {
        List<ErrorDetail> details = new ArrayList<>();
        details.add(new ErrorDetail("roomSize", AppConstants.INVALID_ROOM_SIZE));
        details.add(new ErrorDetail("coords", AppConstants.INVALID_COORDINATES));

        ErrorResponse errorResponse =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        AppConstants.ERROR_VALIDATION_FAILED,
                        details
                        );

        Assertions.assertNotNull(errorResponse);
        Assertions.assertNotNull(errorResponse.getTimestamp());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getError());
        Assertions.assertEquals(AppConstants.ERROR_VALIDATION_FAILED, errorResponse.getMessage());
        Assertions.assertEquals(2, errorResponse.getDetail().size());
        Assertions.assertEquals(2, errorResponse.getDetail().size());
        Assertions.assertTrue(errorResponse.getDetail().get(0).getIssue().contains(AppConstants.INVALID_ROOM_SIZE));
        Assertions.assertTrue(errorResponse.getDetail().get(1).getIssue().contains(AppConstants.INVALID_COORDINATES));
    }
}
