package dev.sumeragizzz.attendancereportconverter;

import dev.sumeragizzz.attendancereportconverter.domain.Attendance;
import dev.sumeragizzz.attendancereportconverter.service.AttendanceReportConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class AttendanceReportConverterApplication implements CommandLineRunner {

	@Autowired
	private AttendanceReportConverterService service;

	public static void main(String... args) {
		SpringApplication.run(AttendanceReportConverterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		service.convert();
	}
}
