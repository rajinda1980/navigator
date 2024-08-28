package org.phinxt.navigator.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.phinxt.navigator.advice.ValidatorException;
import org.phinxt.navigator.dto.HooverRequest;
import org.phinxt.navigator.dto.ValidationError;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppValidatorTest {

    @InjectMocks
    private AppValidator appValidator;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldReturnValidationException() {
        // For room size
        HooverRequest rs_negative1 = new HooverRequest(List.of(-5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest rs_negative2 = new HooverRequest(List.of(5, -5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest rs_negative3 = new HooverRequest(List.of(-5, -5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ValidationError> rs_expect_list_neg = new ArrayList<>();
        rs_expect_list_neg.add(new ValidationError(TestConstants.FIELD_NAME_ROOM_SIZE, TestConstants.NEGATIVE_ROOM_SIZE));
        ValidatorException rs_exp_neg = new ValidatorException(rs_expect_list_neg);

        HooverRequest rs_zero4 = new HooverRequest(List.of(0, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest rs_zero5 = new HooverRequest(List.of(5, 0), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest rs_zero6 = new HooverRequest(List.of(0, 0), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ValidationError> rs_expect_list_zero = new ArrayList<>();
        rs_expect_list_zero.add(new ValidationError(TestConstants.FIELD_NAME_ROOM_SIZE, TestConstants.ZERO_ROOM_SIZE));
        ValidatorException rs_exp_zero = new ValidatorException(rs_expect_list_zero);

        HooverRequest rs_neg_zero7 = new HooverRequest(List.of(-5, 0), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest rs_neg_zero8 = new HooverRequest(List.of(0, -5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ValidationError> rs_expect_list_neg_zero = new ArrayList<>();
        rs_expect_list_neg_zero.add(new ValidationError(TestConstants.FIELD_NAME_ROOM_SIZE, TestConstants.NEGATIVE_ROOM_SIZE));
        rs_expect_list_neg_zero.add(new ValidationError(TestConstants.FIELD_NAME_ROOM_SIZE, TestConstants.ZERO_ROOM_SIZE));
        ValidatorException rs_exp_neg_zero = new ValidatorException(rs_expect_list_neg_zero);

        // For coords
        HooverRequest co_negative1 = new HooverRequest(List.of(5, 5), List.of(-1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest co_negative2 = new HooverRequest(List.of(5, 5), List.of(1, -2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest co_negative3 = new HooverRequest(List.of(5, 5), List.of(-1, -2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ValidationError> co_expect_list_neg = new ArrayList<>();
        co_expect_list_neg.add(new ValidationError(TestConstants.FIELD_NAME_COORDS, TestConstants.NEGATIVE_COORDS));
        ValidatorException co_exp_neg = new ValidatorException(co_expect_list_neg);

        // For patches
        HooverRequest pa_negative1 = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(-1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest pa_negative2 = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, -2), List.of(2, 3)), "NNESEESWNWW");
        HooverRequest pa_negative3 = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(-2, -3)), "NNESEESWNWW");
        List<ValidationError> pa_expect_list_neg = new ArrayList<>();
        pa_expect_list_neg.add(new ValidationError(TestConstants.FIELD_NAME_PATCHES, TestConstants.NEGATIVE_PATCHES));
        ValidatorException pa_exp_neg = new ValidatorException(pa_expect_list_neg);

        // For instructions
        HooverRequest in_negative1 = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNEMXSEESWNWWP");
        List<ValidationError> in_expect_list_neg = new ArrayList<>();
        in_expect_list_neg.add(new ValidationError(TestConstants.FIELD_NAME_INSTRUCTIONS, TestConstants.INVALID_INSTRUCTION_PATTERN));
        ValidatorException in_exp_neg = new ValidatorException(in_expect_list_neg);

        // For all
        HooverRequest all = new HooverRequest(List.of(-5, 0), List.of(1, -2), List.of(List.of(1, 0), List.of(-2, -2), List.of(2, 3)), "NNESAXEEPSWNWW");
        List<ValidationError> all_list = new ArrayList<>();
        all_list.add(new ValidationError(TestConstants.FIELD_NAME_ROOM_SIZE, TestConstants.NEGATIVE_ROOM_SIZE));
        all_list.add(new ValidationError(TestConstants.FIELD_NAME_COORDS, TestConstants.NEGATIVE_COORDS));
        all_list.add(new ValidationError(TestConstants.FIELD_NAME_PATCHES, TestConstants.NEGATIVE_PATCHES));
        all_list.add(new ValidationError(TestConstants.FIELD_NAME_INSTRUCTIONS, TestConstants.INVALID_INSTRUCTION_PATTERN));
        all_list.add(new ValidationError(TestConstants.FIELD_NAME_ROOM_SIZE, TestConstants.ZERO_ROOM_SIZE));
        ValidatorException all_ex = new ValidatorException(all_list);


        return Stream.of(
                Arguments.of(rs_negative1, rs_exp_neg, 1),
                Arguments.of(rs_negative2, rs_exp_neg, 1),
                Arguments.of(rs_negative3, rs_exp_neg, 1),
                Arguments.of(rs_zero4, rs_exp_zero, 1),
                Arguments.of(rs_zero5, rs_exp_zero, 1),
                Arguments.of(rs_zero6, rs_exp_zero, 1),
                Arguments.of(rs_neg_zero7, rs_exp_neg_zero, 2),
                Arguments.of(rs_neg_zero8, rs_exp_neg_zero, 2),

                Arguments.of(co_negative1, co_exp_neg, 1),
                Arguments.of(co_negative2, co_exp_neg, 1),
                Arguments.of(co_negative3, co_exp_neg, 1),

                Arguments.of(pa_negative1, pa_exp_neg, 1),
                Arguments.of(pa_negative2, pa_exp_neg, 1),
                Arguments.of(pa_negative3, pa_exp_neg, 1),

                Arguments.of(in_negative1, in_exp_neg, 1),
                Arguments.of(all, all_ex, 5)
        );
    }

    @DisplayName("Validate Hoover Request object")
    @ParameterizedTest
    @MethodSource
    void shouldReturnValidationException(HooverRequest request, ValidatorException expect, Integer msgCount) {
        ValidatorException result = Assertions.assertThrows(ValidatorException.class, () -> appValidator.validatePayload(request));
        List<ValidationError> list = result.getMessages();
        Assertions.assertEquals(msgCount, list.size());

        List<ValidationError> s1 = list.stream().sorted(Comparator.comparing(ValidationError::getMessage)).collect(Collectors.toList());
        List<ValidationError> s2 = expect.getMessages().stream().sorted(Comparator.comparing(ValidationError::getMessage)).collect(Collectors.toList());
        Assertions.assertEquals(s2, s1);
    }
}
