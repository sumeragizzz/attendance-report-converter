package dev.sumeragizzz.attendancereportconverter;

import dev.sumeragizzz.attendancereportconverter.service.AttendanceReportConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.YearMonth;

@SpringBootApplication
@EnableConfigurationProperties
public class AttendanceReportConverterApplication implements CommandLineRunner {

	@Autowired
	AttendanceReportConverterService service;

	@Autowired
	ParameterConfiguration config;

	public static void main(String... args) {
		SpringApplication.run(AttendanceReportConverterApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// TODO 対象年月はコマンドラインパラメーターから取得する
		YearMonth targetYearMonth = config.targetYearMonth;
		service.convert(targetYearMonth);
	}

}
