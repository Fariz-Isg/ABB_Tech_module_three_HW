package org.abbtech.module3.exception;

import org.abbtech.module3.exception.base.BaseErrorService;
import org.abbtech.module3.exception.base.BaseException;
import lombok.Getter;

/**
 * Exception class representing custom exceptions related to sample error cases.
 * Inherits from {@link BaseException}, allowing it to utilize error details
 * from {@link BaseErrorService} and optional additional arguments for flexible error message formatting.
 */
@Getter
public class CarException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor for {@link CarException} with a throwable and additional arguments.
     *
     * @param baseErrorService The error service providing error details.
     * @param throwable        The root cause of the exception.
     * @param args             Additional arguments for the error.
     */
    public CarException(BaseErrorService baseErrorService, Throwable throwable, Object... args) {
        super(baseErrorService, throwable, args);
    }

    /**
     * Constructor for {@link CarException} with additional arguments.
     *
     * @param baseErrorService The error service providing error details.
     * @param args             Additional arguments for the error.
     */
    public CarException(BaseErrorService baseErrorService, Object... args) {
        super(baseErrorService, args);
    }
}

