package com.parvis.model;

import lombok.Builder;

import java.time.OffsetTime;

@Builder
public record Shift (OffsetTime start, OffsetTime end) {}
