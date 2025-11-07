package com.parvis.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Shift {
    DAY("day"),
    NIGHT("night");

    private final String value;

    Shift(String value) {
        this.value = value;
    }
}
