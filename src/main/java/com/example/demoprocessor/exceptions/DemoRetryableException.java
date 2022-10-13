package com.example.demoprocessor.exceptions;

import org.apache.kafka.common.errors.RetriableException;

public class DemoRetryableException extends RetriableException {
    public DemoRetryableException() {
        super();
    }

    public DemoRetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoRetryableException(String message) {
        super(message);
    }

    public DemoRetryableException(Throwable cause) {
        super(cause);
    }
}
