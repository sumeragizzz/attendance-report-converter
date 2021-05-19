package dev.sumeragizzz.attendancereportconverter.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public record Attendance(LocalDate targetedOn, LocalTime startedAt, LocalTime endedAt, Duration workingHours) {
    public Attendance {
        Objects.requireNonNull(targetedOn);
    }
}
