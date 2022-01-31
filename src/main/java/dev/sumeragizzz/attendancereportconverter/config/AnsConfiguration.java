package dev.sumeragizzz.attendancereportconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConfigurationProperties("ans")
public class AnsConfiguration {
    Path templateFile;

    Path outputFile;

    public Path getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(Path templateFile) {
        this.templateFile = templateFile;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public String toString() {
        return "AnsConfiguration{" +
                "templateFile=" + templateFile +
                ", outputFile=" + outputFile +
                '}';
    }
}
