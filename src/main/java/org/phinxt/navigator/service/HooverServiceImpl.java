package org.phinxt.navigator.service;

import org.apache.logging.log4j.util.Strings;
import org.phinxt.navigator.dto.HooverRequest;
import org.phinxt.navigator.dto.HooverResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HooverServiceImpl implements HooverService {

    public HooverResponse cleanRoom(HooverRequest request) throws Exception {
        List<Integer> roomSize = request.getRoomSize();
        List<Integer> startCoords = request.getCoords();
        List<List<Integer>> patches = null == request.getPatches() ? new ArrayList<>() : request.getPatches();
        String instructions = null == request.getInstructions() ? Strings.EMPTY : request.getInstructions();

        Set<List<Integer>> dirtPatches = new HashSet<>(patches);
        int cleanedPatches = 0;
        int x = startCoords.get(0);
        int y = startCoords.get(1);
        char[] directions = instructions.toCharArray();

        for (char d : directions) {
            switch (d) {
                case 'N': if (y < roomSize.get(1) - 1) y++; break;
                case 'S': if (y > 0) y--; break;
                case 'E': if (x < roomSize.get(0) - 1) x++; break;
                case 'W': if (x > 0) x--; break;
            }

            // Coordinates of x and y for the current direction
            List<Integer> currentPos = Arrays.asList(x, y);
            // Remove the coordinate if it is cleaned
            if (dirtPatches.contains(currentPos)) {
                dirtPatches.remove(currentPos);
                cleanedPatches++;
            }
        }

        // Prepare the response
        HooverResponse response = new HooverResponse();
        response.setCoords(Arrays.asList(x, y));
        response.setPatches(cleanedPatches);

        return response;
    }
}
