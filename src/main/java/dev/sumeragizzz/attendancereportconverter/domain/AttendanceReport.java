package dev.sumeragizzz.attendancereportconverter.domain;

import java.time.Duration;
import java.time.YearMonth;
import java.util.List;

public record AttendanceReport(YearMonth yearMonth, List<Attendance> attendanceList, Duration totalWorkingHours) {

    public AttendanceReport(List<Attendance> attendanceList) {
        this(null, attendanceList, null);
    }

    private extractYearMonth(List<Attendance> attendanceList) {

    }

}
