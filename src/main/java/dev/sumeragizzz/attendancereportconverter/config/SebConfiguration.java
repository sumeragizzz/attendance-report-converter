package dev.sumeragizzz.attendancereportconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.net.URL;

@ConfigurationProperties("seb")
@ConstructorBinding
public final class SebConfiguration {
    final URL url;

    final String id;

    final String password;

    public SebConfiguration(URL url, String id, String password) {
        this.url = url;
        this.id = id;
        this.password = password;
    }

    public URL getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SebConfiguration{" +
                "url=" + url +
                ", id='" + id + '\'' +
                '}';
    }
}
