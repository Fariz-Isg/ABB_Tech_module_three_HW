package org.abbtech.module3.logger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Profile("prod")
public class ProdLoggerService implements LoggerService {

    @Override
    public void logRequest(String endpoint, Object data) {
        System.out.println("PROD " + LocalDateTime.now() + "  REQUEST  " + endpoint);
    }

    @Override
    public void logResponse(String endpoint, Object data) {
        System.out.println("PROD " + LocalDateTime.now() + "  RESPONSE  " + endpoint);
    }

    @Override
    public void logError(String endpoint, String error) {
        System.out.println("PROD " + LocalDateTime.now() + "  ERROR  " + endpoint + "  " + error);
    }
}