package dev.sumeragizzz.attendancereportconverter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.file.Path;
import java.time.YearMonth;

@Component
@ConfigurationProperties("parameter")
public class ParameterConfiguration {

    YearMonth targetYearMonth = YearMonth.now();

    URL url;

    String id;

    String password;

    Path reportFile;

}
