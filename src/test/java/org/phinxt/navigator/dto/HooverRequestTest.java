package org.phinxt.navigator.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.phinxt.navigator.utils.AppConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class HooverRequestTest {

    private Validator validator;
    private HooverRequest request;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        request = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
    }

    @DisplayName("The HooverRequest object should be processed without any validation errors")
    @Test
    void shouldProcessRequestForValidHooverRequest() {
        Set<ConstraintViolation<HooverRequest>> violations = validator.validate(request);
        Assertions.assertTrue(violations.isEmpty());
    }

    /**
     * Value map to negative scenarios for HooverRequest processing
     * @return argument list
     */
    static Stream<Arguments> shouldNotProcessHooverRequest() {
        HooverRequest rs_null = new HooverRequest(null, List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest rs_empty = new HooverRequest(new ArrayList<>(), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest coords_null = new HooverRequest(List.of(5, 5), null, List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest coords_empty = new HooverRequest(List.of(5, 5), new ArrayList<>(), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");

        return Stream.of(
                Arguments.of(rs_null, List.of(AppConstants.INVALID_ROOM_SIZE)),
                Arguments.of(rs_empty, List.of(AppConstants.INVALID_ROOM_SIZE)),
                Arguments.of(coords_null, List.of(AppConstants.INVALID_COORDINATES)),
                Arguments.of(coords_empty, List.of(AppConstants.INVALID_COORDINATES))
        );
    }

    @DisplayName("The HooverRequest object should not be processed if there are one or more exceptions")
    @ParameterizedTest
    @MethodSource
    void shouldNotProcessHooverRequest(HooverRequest request, List<String> expected) {
        Set<ConstraintViolation<HooverRequest>> violations = validator.validate(request);
        Assertions.assertFalse(violations.isEmpty());

        for (ConstraintViolation<HooverRequest> violation : violations) {
            String msg = String.valueOf(violation.getMessage());
            Assertions.assertTrue(expected.contains(msg));
        }
    }
}
