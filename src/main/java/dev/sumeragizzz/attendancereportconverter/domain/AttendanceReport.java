package dev.sumeragizzz.attendancereportconverter.domain;

import java.time.Duration;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;

public class AttendanceReport {

    final List<Attendance> attendances;

    final YearMonth yearMonth;

    final Duration totalWorkingHours;

    public AttendanceReport(List<Attendance> attendances) {
        this.attendances = Objects.requireNonNull(attendances);
        this.yearMonth = extractYearMonth(attendances);
        this.totalWorkingHours = summaryTotalWorkingHours(attendances);
    }

    YearMonth extractYearMonth(List<Attendance> attendances) {
        List<YearMonth> yearMonths = attendances.stream()
                .map(Attendance::targetedOn)
                .map(YearMonth::from)
                .distinct()
                .toList();
        if (yearMonths.size() != 1) {
            throw new IllegalArgumentException();
        }
        return yearMonths.get(0);
    }

    Duration summaryTotalWorkingHours(List<Attendance> attendances) {
        return attendances.stream()
                .map(Attendance::workingHours)
                .filter(Objects::nonNull)
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

    @Override
    public String toString() {
        return "AttendanceReport{" +
                "attendances=" + attendances +
                ", yearMonth=" + yearMonth +
                ", totalWorkingHours=" + totalWorkingHours +
                '}';
    }
}
