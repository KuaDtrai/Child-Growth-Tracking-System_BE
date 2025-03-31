package G5_SWP391.ChildGrownTracking.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum membership {
    BASIC("BASIC"),
    PREMIUM("PREMIUM");

    private final String value;

    membership(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

