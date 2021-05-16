package dev.sumeragizzz.attendancereportconverter;

import dev.sumeragizzz.attendancereportconverter.service.AttendanceReportConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.YearMonth;
import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties
public class AttendanceReportConverterApplication implements CommandLineRunner {

	static final Logger LOGGER = LoggerFactory.getLogger(AttendanceReportConverterApplication.class);

	@Autowired
	AttendanceReportConverterService service;

	@Autowired
	ParameterConfiguration config;

	public static void main(String... args) {
		SpringApplication.run(AttendanceReportConverterApplication.class, args);
	}

	@Override
	public void run(String... args) {
		LOGGER.info(config.toString());
		service.convert(config.targetYearMonth);
	}

}
