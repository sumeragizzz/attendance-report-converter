package dev.sumeragizzz.attendancereportconverter.service;

import dev.sumeragizzz.attendancereportconverter.domain.Attendance;
import dev.sumeragizzz.attendancereportconverter.domain.AttendanceReport;
import dev.sumeragizzz.attendancereportconverter.repository.AnsAttendanceReportRepository;
import dev.sumeragizzz.attendancereportconverter.repository.SebAttendanceReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AttendanceReportConverterService {

    @Autowired
    SebAttendanceReportRepository sebRepository;

    @Autowired
    AnsAttendanceReportRepository ansRepository;

    public void convert(YearMonth targetYearMonth) {
        AttendanceReport attendanceReport = sebRepository.findByYearMonth(targetYearMonth).orElseThrow();
        AttendanceReport revisedAttendanceReport = revise(attendanceReport);
        ansRepository.store(revisedAttendanceReport);
    }

    AttendanceReport revise(AttendanceReport attendanceReport) {
        List<Attendance> attendances = new ArrayList<>();
        for (Attendance attendance : attendanceReport.getAttendances()) {
            Attendance revisedAttendance = attendance.getEndedAt()
                    .filter(endedAt -> endedAt.isAfter(LocalTime.of(18, 0)))
                    .map(endedAt -> attendance.withEndedAt(endedAt.plus(Duration.ofMinutes(10))))
                    .orElse(attendance);
            attendances.add(revisedAttendance);
        }
        return new AttendanceReport(attendances);
    }

}
