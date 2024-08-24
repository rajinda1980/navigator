package org.phinxt.navigator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HooverResponse {
    private List<Integer> coords;
    private int patches;
}
