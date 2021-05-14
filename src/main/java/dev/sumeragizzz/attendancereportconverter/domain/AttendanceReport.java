package dev.sumeragizzz.attendancereportconverter.domain;

import java.time.Duration;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AttendanceReport {

    private List<Attendance> attendances;

    private YearMonth yearMonth;

    private Duration totalWorkingHours;

    public AttendanceReport(List<Attendance> attendances) {
        this.attendances = Objects.requireNonNull(attendances);
        this.yearMonth = extractYearMonth(attendances);
        this.totalWorkingHours = summaryTotalWorkingHours(attendances);
    }

    private YearMonth extractYearMonth(List<Attendance> attendances) {
        List<YearMonth> yearMonths = attendances.stream()
                .map(Attendance::targetedOn)
                .map(YearMonth::from)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
        if (yearMonths.size() != 1) {
            throw new IllegalArgumentException();
        }
        return yearMonths.get(0);
    }

    private Duration summaryTotalWorkingHours(List<Attendance> attendances) {
        return attendances.stream()
                .map(Attendance::workingHours)
                .collect(Collector.of(AtomicLong::new,
                        (AtomicLong minutes, Duration duration) -> minutes.addAndGet(duration.toMinutes()),
                        (AtomicLong a, AtomicLong b) -> new AtomicLong(a.get() + b.get()),
                        minutes -> Duration.ofMinutes(minutes.get())));
    }

    public List<Attendance> getAttendances() {
        return Collections.unmodifiableList(attendances);
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public Duration getTotalWorkingHours() {
        return totalWorkingHours;
    }

}
