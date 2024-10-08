package org.phinxt.navigator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class TestConstants {
    private TestConstants() {}

    public static final String REQUEST_HOOVER_CLEAN = "/api/v1/hoover/clean";

    public static final String ERROR_VALIDATION_FAILED = "Validation Failed";
    public static final String INVALID_ROOM_SIZE = "Room size must have exactly 2 dimensions";
    public static final String INVALID_COORDINATES = "Coordinates must have exactly 2 values";
    public static final String INVALID_INSTRUCTION = "Instructions cannot be empty";
    public static final String INVALID_CONTENT_TYPE = "Invalid Content-Type. Content-Type must be application/json";

    public static final String NEGATIVE_ROOM_SIZE = "Any dimension of the room size cannot be a negative value";
    public static final String ZERO_ROOM_SIZE = "Any dimension of the room size cannot be a 0 value";
    public static final String NEGATIVE_COORDS = "Coords cannot be a negative value";
    public static final String NEGATIVE_PATCHES = "Patches cannot be a negative value";
    public static final String INVALID_INSTRUCTION_PATTERN = "Invalid instruction pattern";

    public static final String FIELD_NAME_ROOM_SIZE = "roomSize";
    public static final String FIELD_NAME_COORDS = "coords";
    public static final String FIELD_NAME_PATCHES = "patches";
    public static final String FIELD_NAME_INSTRUCTIONS = "instructions";

    public static final String HOST = "localhost";

    public static Gson getFullyFledgedGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                (json, type, jsonDeserializationContext) -> {
                    try{
                        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"));
                    } catch (DateTimeParseException e){
                        return null;
                    }
                }).create();
    }

}
