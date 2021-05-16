package dev.sumeragizzz.attendancereportconverter.repository;

import dev.sumeragizzz.attendancereportconverter.domain.Attendance;
import dev.sumeragizzz.attendancereportconverter.domain.AttendanceReport;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class SebAttendanceReportRepository {

    public Optional<AttendanceReport> findByYearMonth(YearMonth targetYearMonth) {
        // TODO 未実装
        return Optional.of(new AttendanceReport(List.of(new Attendance(
                LocalDate.of(2021, 5, 1),
                LocalTime.of(9, 0),
                LocalTime.of(18,0),
                Duration.ofHours(8)))));
    }

}
