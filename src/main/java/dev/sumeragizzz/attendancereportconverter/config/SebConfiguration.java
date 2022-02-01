package dev.sumeragizzz.attendancereportconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@ConfigurationProperties("seb")
public record SebConfiguration(URL url, String id, String password) {
    @Override
    public String toString() {
        return "SebConfiguration[" +
                "url=" + url +
                ", id='" + id + '\'' +
                ']';
    }
}
