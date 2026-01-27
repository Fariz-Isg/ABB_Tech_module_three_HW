package org.abbtech.module3.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String env;
    private String name;
    private String version;
    private LoggingConfig logging;
    private FeaturesConfig features;

    public void setEnv(String env) {
        this.env = env;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setLogging(LoggingConfig logging) {
        this.logging = logging;
    }

    public void setFeatures(FeaturesConfig features) {
        this.features = features;
    }

    public String getEnv() {
        return env;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public LoggingConfig getLogging() {
        return logging;
    }

    public FeaturesConfig getFeatures() {
        return features;
    }

    public static class LoggingConfig {
        private String level;

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLevel() {
            return level;
        }
    }

    public static class FeaturesConfig {
        private boolean detailedLogging;

        public void setDetailedLogging(boolean detailedLogging) {
            this.detailedLogging = detailedLogging;
        }

        public boolean isDetailedLogging() {
            return detailedLogging;
        }
    }
}