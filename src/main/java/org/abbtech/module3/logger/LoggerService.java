package org.abbtech.module3.logger;

public interface LoggerService {
    void logRequest(String endpoint, Object data);
    void logResponse(String endpoint, Object data);
    void logError(String endpoint, String error);
}
