package com.consulti.api.interfaces;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PlanType {
    ADVANCE("Advance"),
    MIDDLE("Middle"),
    BASIC("Basic");

    private final String value;

    PlanType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
