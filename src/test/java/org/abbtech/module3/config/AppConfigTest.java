package org.abbtech.module3.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppConfig Tests")
public class AppConfigTest {

    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
    }

    @Test
    @DisplayName("Should set and get env")
    void testEnv() {
        appConfig.setEnv("development");
        assertEquals("development", appConfig.getEnv());
    }

    @Test
    @DisplayName("Should set and get name")
    void testName() {
        appConfig.setName("Car Service");
        assertEquals("Car Service", appConfig.getName());
    }

    @Test
    @DisplayName("Should set and get version")
    void testVersion() {
        appConfig.setVersion("1.0.0");
        assertEquals("1.0.0", appConfig.getVersion());
    }

    @Test
    @DisplayName("Should set and get features")
    void testFeatures() {
        AppConfig.FeaturesConfig features = new AppConfig.FeaturesConfig();
        features.setDetailedLogging(true);

        appConfig.setFeatures(features);

        assertNotNull(appConfig.getFeatures());
        assertTrue(appConfig.getFeatures().isDetailedLogging());
    }

    @Test
    @DisplayName("FeaturesConfig - should set and get detailedLogging")
    void testFeaturesConfig() {
        AppConfig.FeaturesConfig features = new AppConfig.FeaturesConfig();

        features.setDetailedLogging(true);
        assertTrue(features.isDetailedLogging());

        features.setDetailedLogging(false);
        assertFalse(features.isDetailedLogging());
    }
}