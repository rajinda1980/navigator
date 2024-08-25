package org.phinxt.navigator.service;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.phinxt.navigator.dto.HooverRequest;
import org.phinxt.navigator.dto.HooverResponse;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HooverServiceImplTest {

    @InjectMocks
    HooverServiceImpl hooverService;

    private HooverRequest request;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        request = new HooverRequest();
        request.setRoomSize(List.of(5, 5));
        request.setCoords(List.of(1, 2));
        request.setPatches(List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)));
        request.setInstructions("NNESEESWNWW");
    }

    @DisplayName("Should return the final coordinates with cleaned patches")
    @Test
    void shouldReturnFinalCoordinateWithCleanedPatches() {
        HooverResponse response = hooverService.cleanRoom(request);
        Assertions.assertEquals(1, response.getPatches());
        Assertions.assertEquals(List.of(1, 3), response.getCoords());
    }

    @DisplayName("Should return coordinates and 0 output patches if input patches are empty")
    @Test
    void shouldReturnCoordWithEmptyInputPatches() {
        request.setPatches(new ArrayList<>());
        HooverResponse response = hooverService.cleanRoom(request);
        Assertions.assertEquals(0, response.getPatches());
        Assertions.assertEquals(List.of(1, 3), response.getCoords());
    }

    @DisplayName("Should return coordinates and 0 output patches if input patches are null")
    @Test
    void shouldReturnCoordWithNullInputPatches() {
        request.setPatches(null);
        HooverResponse response = hooverService.cleanRoom(request);
        Assertions.assertEquals(0, response.getPatches());
        Assertions.assertEquals(List.of(1, 3), response.getCoords());
    }

    @DisplayName("Should return coordinates for empty instructions")
    @Test
    void shouldReturnCoordsForEmptyInstructions() {
        request.setInstructions(Strings.EMPTY);
        HooverResponse response = hooverService.cleanRoom(request);
        Assertions.assertEquals(List.of(1, 2), response.getCoords());
        Assertions.assertEquals(0, response.getPatches());
    }

    @DisplayName("Should return coordinates for null instructions")
    @Test
    void shouldReturnCoordsForNullInstructions() {
        request.setInstructions(null);
        HooverResponse response = hooverService.cleanRoom(request);
        Assertions.assertEquals(List.of(1, 2), response.getCoords());
        Assertions.assertEquals(0, response.getPatches());
    }

    @DisplayName("Should return coordinates and patches for boundary checks")
    @Test
    void shouldReturnCoordsAndPatchesForBoundaryCheck() {
        request.setRoomSize(List.of(3, 3));
        request.setCoords(List.of(1, 1));
        request.setPatches(
                List.of(
                        List.of(0, 0),
                        List.of(2, 2)
                )
        );
        request.setInstructions("NNNNEEEE");
        HooverResponse response = hooverService.cleanRoom(request);
        Assertions.assertEquals(List.of(2, 2), response.getCoords());
        Assertions.assertEquals(1, response.getPatches());
    }
}
