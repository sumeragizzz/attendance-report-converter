package dev.sumeragizzz.attendancereportconverter.service;

import dev.sumeragizzz.attendancereportconverter.domain.AttendanceReport;
import dev.sumeragizzz.attendancereportconverter.repository.AnsAttendanceReportRepository;
import dev.sumeragizzz.attendancereportconverter.repository.SebAttendanceReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class AttendanceReportConverterService {

    @Autowired
    SebAttendanceReportRepository sebRepository;

    @Autowired
    AnsAttendanceReportRepository ansRepository;

    public void convert(YearMonth targetYearMonth) {
        AttendanceReport attendanceReport = sebRepository.findByYearMonth(targetYearMonth).orElseThrow();
        ansRepository.store(attendanceReport);
    }

}
