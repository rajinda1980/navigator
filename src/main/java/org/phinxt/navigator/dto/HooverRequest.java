package org.phinxt.navigator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.phinxt.navigator.utils.AppConstants;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HooverRequest {

    @NotNull(message = AppConstants.INVALID_ROOM_SIZE)
    @Size(min = 2, max = 2, message = AppConstants.INVALID_ROOM_SIZE)
    private List<Integer> roomSize;

    @NotNull(message = AppConstants.INVALID_COORDINATES)
    @Size(min = 2, max = 2, message = AppConstants.INVALID_COORDINATES)
    private List<Integer> coords;

    private List<List<Integer>> patches;
    private String instructions;
}
