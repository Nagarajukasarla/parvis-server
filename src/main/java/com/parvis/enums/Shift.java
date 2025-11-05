package com.parvis.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Shift {
    DAY("day"),
    NIGHT("night");

    private final String value;
}
