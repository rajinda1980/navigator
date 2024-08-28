package org.phinxt.navigator.utils;

public final class AppConstants {
    private AppConstants() {}

    public static final String INVALID_ROOM_SIZE = "Room size must have exactly 2 dimensions";
    public static final String INVALID_COORDINATES = "Coordinates must have exactly 2 values";
    public static final String INVALID_INSTRUCTION = "Instructions cannot be empty";

    public static final String NEGATIVE_ROOM_SIZE = "Any dimension of the room size cannot be a negative value";
    public static final String ZERO_ROOM_SIZE = "Any dimension of the room size cannot be a 0 value";
    public static final String NEGATIVE_COORDS = "Coords cannot be a negative value";
    public static final String NEGATIVE_PATCHES = "Patches cannot be a negative value";
    public static final String INVALID_INSTRUCTION_PATTERN = "Invalid instruction pattern";

    public static final String FIELD_NAME_ROOM_SIZE = "roomSize";
    public static final String FIELD_NAME_COORDS = "coords";
    public static final String FIELD_NAME_PATCHES = "patches";
    public static final String FIELD_NAME_INSTRUCTIONS = "instructions";

    public static final String ERROR_VALIDATION_FAILED = "Validation Failed";
    public static final String INVALID_CONTENT_TYPE = "Invalid Content-Type. Content-Type must be application/json";
}
