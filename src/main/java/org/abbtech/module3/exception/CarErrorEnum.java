package org.abbtech.module3.exception;

import org.abbtech.module3.exception.base.BaseErrorService;

public enum CarErrorEnum implements BaseErrorService {

    CAR_NOT_FOUND("CAR-NOT-FOUND-001", "Car not found with given id", 404),
    BRAND_NOT_FOUND("BRAND-NOT-FOUND-002", "Brand not found with given id", 404),
    MODEL_NOT_FOUND("MODEL-NOT-FOUND-003", "Model not found with given id", 404),
    BRAND_ALREADY_EXISTS("BRAND-ALREADY-EXISTS-004", "Brand with this name already exists", 409),
    INVALID_CAR_DATA("INVALID-CAR-DATA-005", "Invalid car data provided", 400),
    INVALID_BRAND_DATA("INVALID-BRAND-DATA-006", "Invalid brand data provided", 400),
    INVALID_MODEL_DATA("INVALID-MODEL-DATA-007", "Invalid model data provided", 400);

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