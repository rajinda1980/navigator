package org.phinxt.navigator.controller;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.phinxt.navigator.dto.ErrorDetail;
import org.phinxt.navigator.dto.ErrorResponse;
import org.phinxt.navigator.dto.HooverRequest;
import org.phinxt.navigator.dto.HooverResponse;
import org.phinxt.navigator.utils.TestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HooverControllerEtoETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldReturnCoordsAndPatchesForValidInput() {
        HooverRequest request = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), "NNESEESWNWW");
        HooverResponse response = new HooverResponse(List.of(1, 3), 1);

        HooverRequest rp_empty = new HooverRequest(List.of(5, 5), List.of(1, 2), new ArrayList<>(), "NNESEESWNWW");
        HooverResponse respa_empty = new HooverResponse(List.of(1, 3), 0);

        HooverRequest rp_null = new HooverRequest(List.of(5, 5), List.of(1, 2), null, "NNESEESWNWW");
        HooverResponse respa_null = new HooverResponse(List.of(1, 3), 0);

        HooverRequest ins_empty = new HooverRequest(List.of(5, 5), List.of(1, 2), List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), Strings.EMPTY);
        HooverResponse resins_empty = new HooverResponse(List.of(1, 2), 0);

        HooverRequest ins_null = new HooverRequest(List.of(5, 5), List.of(1, 2),  List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)), null);
        HooverResponse resins_null = new HooverResponse(List.of(1, 2), 0);

        return Stream.of(
                Arguments.of(request, response),
                Arguments.of(rp_empty, respa_empty),
                Arguments.of(rp_null, respa_null),
                Arguments.of(ins_empty, resins_empty),
                Arguments.of(ins_null, resins_null)
        );
    }

    @DisplayName("E2E Test - Should return coordinates and patches for valid input")
    @ParameterizedTest
    @MethodSource
    void shouldReturnCoordsAndPatchesForValidInput(HooverRequest request, HooverResponse expect) {
        ResponseEntity<HooverResponse> response = restTemplate.exchange(TestConstants.REQUEST_HOOVER_CLEAN, HttpMethod.POST, setHeaders(request), HooverResponse.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(expect.getPatches(), response.getBody().getPatches());
        Assertions.assertEquals(expect.getCoords(), response.getBody().getCoords());
    }

    /**
     * Value map
     * @return argument list
     */
    static Stream<Arguments> shouldReturnException() {
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

    @DisplayName("E2E Test - Should return details about the exception if the request fails")
    @ParameterizedTest
    @MethodSource
    void shouldReturnException(HooverRequest request, ErrorResponse expect) {
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(TestConstants.REQUEST_HOOVER_CLEAN, HttpMethod.POST, setHeaders(request), ErrorResponse.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(expect.getStatus(), response.getBody().getStatus());
        Assertions.assertEquals(expect.getMessage(), response.getBody().getMessage());
        Assertions.assertEquals(expect.getError(), response.getBody().getError());
        Assertions.assertEquals(expect.getDetail().size(), response.getBody().getDetail().size());
        Assertions.assertEquals(expect.getDetail().get(0).getField(), response.getBody().getDetail().get(0).getField());
        Assertions.assertEquals(expect.getDetail().get(0).getIssue(), response.getBody().getDetail().get(0).getIssue());
    }

    private HttpEntity setHeaders(HooverRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity(request, headers);
    }
}
