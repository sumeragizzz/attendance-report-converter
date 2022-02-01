package dev.sumeragizzz.attendancereportconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties("ans")
public record AnsConfiguration(Path templateFile, Path outputFile) {
}
