package org.abbtech.module3.logger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Profile("dev")
public class DevLoggerService implements LoggerService {

    @Override
    public void logRequest(String endpoint, Object data) {
        System.out.println("DEV REQUEST");
        System.out.println("Timestamp: " + LocalDateTime.now());
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Data: " + data);
        System.out.println("Thread: " + Thread.currentThread().getName());
        System.out.println(" ");
    }

    @Override
    public void logResponse(String endpoint, Object data) {
        System.out.println("DEV RESPONSE");
        System.out.println("Timestamp: " + LocalDateTime.now());
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Data: " + data);
        System.out.println(" ");
    }

    @Override
    public void logError(String endpoint, String error) {
        System.out.println("DEV ERROR");
        System.out.println("Timestamp: " + LocalDateTime.now());
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Error: " + error);
        System.out.println(" ");
    }
}