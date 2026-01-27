package org.abbtech.module3.exception;

import org.abbtech.module3.exception.base.BaseErrorService;

public enum CarErrorEnum implements BaseErrorService {
    CAR_NOT_FOUND("CAR-NOT-FOUND-001", "Car not found with given id", 404),
    CAR_ALREADY_EXISTS("CAR-ALREADY-EXISTS-002", "Car with this name already exists", 409),
    INVALID_CAR_DATA("INVALID-CAR-DATA-003", "Invalid car data provided", 400);

    final String message;
    final int httpStatus;
    final String errorCode;

    CarErrorEnum(String errorCode, String message, int httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}

