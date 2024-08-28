package org.phinxt.navigator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.phinxt.navigator.advice.ValidatorException;
import org.phinxt.navigator.dto.*;
import org.phinxt.navigator.service.HooverService;
import org.phinxt.navigator.utils.AppValidator;
import org.phinxt.navigator.utils.TestConstants;
import org.phinxt.navigator.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HooverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HooverService hooverService;

    @MockBean
    private AppValidator appValidator;

    private HooverRequest hooverRequest;
    private String url;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hooverRequest = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        url = TestConstants.REQUEST_HOOVER_CLEAN;
    }

    @DisplayName("Should return a response without errors for valid input")
    @Test
    void shouldReturnSuccessfulResponse() throws Exception {
        HooverResponse response = new HooverResponse(List.of(1, 3), 1);
        Mockito.when(hooverService.cleanRoom(isA(HooverRequest.class))).thenReturn(response);
        MvcResult result =
                mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(hooverRequest))
                ).andReturn();

        Gson gson = new Gson();
        HooverResponse fromJson = gson.fromJson(result.getResponse().getContentAsString(), HooverResponse.class);
        Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        List<Integer> coords = List.of(1, 3);
        Assertions.assertEquals(coords, fromJson.getCoords());
        Assertions.assertEquals(1, fromJson.getPatches());
    }

    @DisplayName("Should return a response in JSON format")
    @Test
    void shouldReturnSuccessfulJSONResponse() throws Exception {
        HooverResponse response = new HooverResponse(List.of(1, 3), 1);
        Mockito.when(hooverService.cleanRoom(isA(HooverRequest.class))).thenReturn(response);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(hooverRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    /**
     * Value map - For error response
     * @return argument list
     */
    static Stream<Arguments> shouldReturnErrorResponse() {
        HooverRequest rs_null = new HooverRequest(null, List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ErrorDetail> rs_null_dt = List.of(new ErrorDetail("roomSize", TestConstants.INVALID_ROOM_SIZE));
        ErrorResponse rs_null_error =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        TestConstants.ERROR_VALIDATION_FAILED, rs_null_dt);

        HooverRequest rs_empty = new HooverRequest(new ArrayList<>(), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ErrorDetail> rs_empty_dt = List.of(new ErrorDetail("roomSize", TestConstants.INVALID_ROOM_SIZE));
        ErrorResponse rs_empty_error =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        TestConstants.ERROR_VALIDATION_FAILED, rs_empty_dt);

        HooverRequest coords_null = new HooverRequest(List.of(5, 5), null, List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ErrorDetail> coords_null_dt = List.of(new ErrorDetail("coords", TestConstants.INVALID_COORDINATES));
        ErrorResponse coords_null_error =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        TestConstants.ERROR_VALIDATION_FAILED, coords_null_dt);

        HooverRequest coords_empty = new HooverRequest(List.of(5, 5), new ArrayList<>(), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        List<ErrorDetail> coords_empty_dt = List.of(new ErrorDetail("coords", TestConstants.INVALID_COORDINATES));
        ErrorResponse coords_empty_error =
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        TestConstants.ERROR_VALIDATION_FAILED, coords_empty_dt);

        return Stream.of(
                Arguments.of(rs_null, rs_null_error),
                Arguments.of(rs_empty, rs_empty_error),
                Arguments.of(coords_null, coords_null_error),
                Arguments.of(coords_empty, coords_empty_error)
        );
    }

    @DisplayName("Should return an error response for an invalid request")
    @ParameterizedTest
    @MethodSource
    void shouldReturnErrorResponse(HooverRequest request, ErrorResponse expect) throws Exception {
        MvcResult result =
                mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(request))
                ).andReturn();

        Gson gson = new Gson();
        ErrorResponse errorResponse = TestConstants.getFullyFledgedGson().fromJson(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        Assertions.assertEquals(expect.getStatus(), errorResponse.getStatus());
        Assertions.assertEquals(expect.getError(), errorResponse.getError());
        Assertions.assertEquals(expect.getMessage(), errorResponse.getMessage());
        Assertions.assertEquals(expect.getDetail(), errorResponse.getDetail());
    }

    @DisplayName("Should return an invalid content type error")
    @Test
    void shouldReturnContentTypeErrorMessage() throws Exception {
       MvcResult result =
                    mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post(url)
                                    .contentType(MediaType.APPLICATION_XML_VALUE)
                                    .content(objectMapper.writeValueAsString(hooverRequest))
                    ).andReturn();
       Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
       Assertions.assertEquals(AppConstants.INVALID_CONTENT_TYPE, result.getResponse().getContentAsString());
    }

    @DisplayName("Should return a Method Not Allowed exception for an invalid method")
    @Test
    void shouldReturnMethodNotAllowedMessage() throws Exception {
        MvcResult result =
                mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(url)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(hooverRequest))
                ).andReturn();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());
    }

    @DisplayName("Should return a Exception if service class throws an exception")
    @Test
    void shouldReturnException() throws Exception {
        Mockito.when(hooverService.cleanRoom(any(HooverRequest.class))).thenThrow(new RuntimeException("Unknown Exception"));
        MvcResult result =
                mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(hooverRequest))
                ).andReturn();
        Gson gson = new Gson();
        ErrorResponse errorResponse = TestConstants.getFullyFledgedGson().fromJson(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getError());
        Assertions.assertEquals("Unknown Exception", errorResponse.getMessage());
        Assertions.assertTrue(errorResponse.getDetail().isEmpty());
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
                    Arguments.of(rs_negative1, rs_exp_neg, rs_expect_list_neg),
                    Arguments.of(rs_negative2, rs_exp_neg, rs_expect_list_neg),
                    Arguments.of(rs_negative3, rs_exp_neg, rs_expect_list_neg),
                    Arguments.of(rs_zero4, rs_exp_zero, rs_expect_list_zero),
                    Arguments.of(rs_zero5, rs_exp_zero, rs_expect_list_zero),
                    Arguments.of(rs_zero6, rs_exp_zero, rs_expect_list_zero),
                    Arguments.of(rs_neg_zero7, rs_exp_neg_zero, rs_expect_list_neg_zero),
                    Arguments.of(rs_neg_zero8, rs_exp_neg_zero, rs_expect_list_neg_zero),

                    Arguments.of(co_negative1, co_exp_neg, co_expect_list_neg),
                    Arguments.of(co_negative2, co_exp_neg, co_expect_list_neg),
                    Arguments.of(co_negative3, co_exp_neg, co_expect_list_neg),

                    Arguments.of(pa_negative1, pa_exp_neg, pa_expect_list_neg),
                    Arguments.of(pa_negative2, pa_exp_neg, pa_expect_list_neg),
                    Arguments.of(pa_negative3, pa_exp_neg, pa_expect_list_neg),

                    Arguments.of(in_negative1, in_exp_neg, in_expect_list_neg),
                    Arguments.of(all, all_ex, all_list)
                );
    }


    @DisplayName("Should return validation exception for HooverRequest payload")
    @ParameterizedTest
    @MethodSource
    void shouldReturnValidationException(HooverRequest request, ValidatorException exception, List<ValidationError> expected) throws Exception {
        Mockito.doThrow(exception).when(appValidator).validatePayload(request);
        MvcResult result =
                mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(request))
                ).andReturn();

        Gson gson = new Gson();
        ErrorResponse errorResponse = TestConstants.getFullyFledgedGson().fromJson(result.getResponse().getContentAsString(), ErrorResponse.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        List<ErrorDetail> sexpect =
                expected.stream()
                        .map(e -> new ErrorDetail(e.getFieldName(), e.getMessage()))
                        .sorted(Comparator.comparing(ErrorDetail::getIssue))
                        .collect(Collectors.toList());
        List<ErrorDetail> errors = errorResponse.getDetail().stream().sorted(Comparator.comparing(ErrorDetail::getIssue)).collect(Collectors.toList());
        Assertions.assertEquals(sexpect, errors);
    }
}
