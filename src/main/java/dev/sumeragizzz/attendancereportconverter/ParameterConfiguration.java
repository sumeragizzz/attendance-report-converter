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

    public YearMonth getTargetYearMonth() {
        return targetYearMonth;
    }

    public void setTargetYearMonth(YearMonth targetYearMonth) {
        this.targetYearMonth = targetYearMonth;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Path getReportFile() {
        return reportFile;
    }

    public void setReportFile(Path reportFile) {
        this.reportFile = reportFile;
    }

    @Override
    public String toString() {
        return "ParameterConfiguration{" +
                "targetYearMonth=" + targetYearMonth +
                ", url=" + url +
                ", id='" + id + '\'' +
                ", reportFile=" + reportFile +
                '}';
    }

}
