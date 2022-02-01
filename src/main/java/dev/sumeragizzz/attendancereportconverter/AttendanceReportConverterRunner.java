package dev.sumeragizzz.attendancereportconverter;

import dev.sumeragizzz.attendancereportconverter.config.AnsConfiguration;
import dev.sumeragizzz.attendancereportconverter.config.SebConfiguration;
import dev.sumeragizzz.attendancereportconverter.service.AttendanceReportConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.function.Function;

@Component
public class AttendanceReportConverterRunner implements ApplicationRunner {
    static final Logger LOGGER = LoggerFactory.getLogger(AttendanceReportConverterRunner.class);

    @Autowired
    AttendanceReportConverterService service;

    @Autowired
    SebConfiguration sebConfiguration;

    @Autowired
    AnsConfiguration ansConfiguration;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info(String.valueOf(sebConfiguration));
        LOGGER.info(String.valueOf(ansConfiguration));
        // パラメーター対象年月をチェック
        var targetYearMonth = validateArgument(args, "targetYearMonth", YearMonth::parse);
        LOGGER.info("start. targetYearMonth: {}", targetYearMonth);
        // 変換実行
        service.convert(targetYearMonth);
        LOGGER.info("end.");
    }

    <T> T validateArgument(ApplicationArguments args, String name, Function<String, T> function) {
        // 必須チェック
        if (!args.containsOption(name)) {
            LOGGER.error("parameter {} is mandatory.", name);
            throw new IllegalArgumentException();
        }
        var values = args.getOptionValues(name);
        // 単一指定チェック
        if (values.size() != 1) {
            LOGGER.error("only one parameter {} can be specified.", name);
            throw new IllegalArgumentException();
        }
        return function.apply(values.get(0));
    }
}
