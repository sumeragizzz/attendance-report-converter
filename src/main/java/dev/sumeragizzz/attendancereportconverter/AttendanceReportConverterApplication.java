package dev.sumeragizzz.attendancereportconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AttendanceReportConverterApplication {
	public static void main(String... args) {
		SpringApplication.run(AttendanceReportConverterApplication.class, args);
	}
}
