package dev.sumeragizzz.attendancereportconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.file.Path;
import java.time.YearMonth;

@Component
@ConfigurationProperties("seb")
public class SebConfiguration {
    URL url;

    String id;

    String password;

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

    @Override
    public String toString() {
        return "SebConfiguration{" +
                "url=" + url +
                ", id='" + id + '\'' +
                '}';
    }
}
