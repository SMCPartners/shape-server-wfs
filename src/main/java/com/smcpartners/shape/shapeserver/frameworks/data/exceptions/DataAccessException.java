package com.smcpartners.shape.shapeserver.frameworks.data.exceptions;

/**
 * Responsible:<br/>
 * 1. Thrown by data components when an exception occurs<br/>
 * <p>
 * Created by johndestefano on 9/10/15.
 * <p>
 * Changes:<b/>
 */
public class DataAccessException extends Exception {
    public DataAccessException() {
        super();
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    protected DataAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
