package dev.sumeragizzz.attendancereportconverter.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record Attendance(LocalDate targetedOn, LocalTime startedAt, LocalTime endedAt, Duration workingHours) {
}
