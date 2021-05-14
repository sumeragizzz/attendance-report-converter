package dev.sumeragizzz.attendancereportconverter.repository;

import dev.sumeragizzz.attendancereportconverter.domain.AttendanceReport;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.Optional;

@Repository
public class SebAttendanceReportRepository {

    public Optional<AttendanceReport> findByYearMonth(YearMonth targetYearMonth) {
        // TODO 未実装
        return Optional.empty();
    }

}
