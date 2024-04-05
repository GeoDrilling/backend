package ru.nsu.fit.geodrilling.entity.projectstate.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumType {
    ORIENTATION,
    SCALE,
    BOOLEAN;
    @JsonValue
    public int toInt() {
        return ordinal();
    }
}
