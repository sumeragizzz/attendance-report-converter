package dev.sumeragizzz.attendancereportconverter.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class Attendance {

    final LocalDate targetedOn;

    final LocalTime startedAt;

    final LocalTime endedAt;

    final Duration workingHours;

    public Attendance(LocalDate targetedOn, LocalTime startedAt, LocalTime endedAt, Duration workingHours) {
        this.targetedOn = Objects.requireNonNull(targetedOn);
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.workingHours = Objects.requireNonNull(workingHours);
    }

    public LocalDate getTargetedOn() {
        return targetedOn;
    }

    public Optional<LocalTime> getStartedAt() {
        return Optional.ofNullable(startedAt);
    }

    public Optional<LocalTime> getEndedAt() {
        return Optional.ofNullable(endedAt);
    }

    public Duration getWorkingHours() {
        return workingHours;
    }

}
