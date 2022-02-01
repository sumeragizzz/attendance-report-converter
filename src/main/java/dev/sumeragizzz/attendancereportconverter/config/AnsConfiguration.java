package dev.sumeragizzz.attendancereportconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.nio.file.Path;

@ConfigurationProperties("ans")
@ConstructorBinding
public final class AnsConfiguration {
    final Path templateFile;

    final Path outputFile;

    public AnsConfiguration(Path templateFile, Path outputFile) {
        this.templateFile = templateFile;
        this.outputFile = outputFile;
    }

    public Path getTemplateFile() {
        return templateFile;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    @Override
    public String toString() {
        return "AnsConfiguration{" +
                "templateFile=" + templateFile +
                ", outputFile=" + outputFile +
                '}';
    }
}
