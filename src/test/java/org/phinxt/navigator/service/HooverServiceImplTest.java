package org.phinxt.navigator.service;

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

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HooverServiceImplTest {

    @InjectMocks
    HooverServiceImpl hooverService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should return final coordinates with cleaned patches")
    @Test
    void shouldReturnFinalCoordinateWithCleanedPatches() {
        HooverRequest request = new HooverRequest();
        request.setRoomSize(List.of(5, 5));
        request.setCoords(List.of(1, 2));
        request.setPatches(List.of(List.of(1, 0), List.of(2, 2), List.of(2, 3)));
        request.setInstructions("NNESEESWNWW");

        HooverResponse response = hooverService.cleanRoom(request);

        Assertions.assertEquals(1, response.getPatches());
        Assertions.assertEquals(List.of(1, 3), response.getCoords());
    }
}
