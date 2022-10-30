package com.example.demoprocessor.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
public class MaxFailuresUncaughtExceptionHandler implements StreamsUncaughtExceptionHandler {
    final int maxFailures;
    final long maxTimeIntervalMillis;
    private Instant previousErrorTime;
    private int currentFailureCount;


    public MaxFailuresUncaughtExceptionHandler(final int maxFailures, final long maxTimeIntervalMillis) {
        this.maxFailures = maxFailures;
        this.maxTimeIntervalMillis = maxTimeIntervalMillis;
    }

    @Override
    public StreamThreadExceptionResponse handle(final Throwable throwable) {
        if (throwable instanceof RetriableException) {
            currentFailureCount++;
            Instant currentErrorTime = Instant.now();

            if (previousErrorTime == null) {
                previousErrorTime = currentErrorTime;
            }

            long millisBetweenFailure = ChronoUnit.MILLIS.between(previousErrorTime, currentErrorTime);

            if (currentFailureCount >= maxFailures) {
                if (millisBetweenFailure <= maxTimeIntervalMillis) {
                    log.info("Shutting down after max failures");
                    return StreamThreadExceptionResponse.SHUTDOWN_CLIENT;
                } else {
                    currentFailureCount = 0;
                    previousErrorTime = null;
                }
            }
            log.info("Changing thread");
            return StreamThreadExceptionResponse.REPLACE_THREAD;
        }
        log.info("Shutting down for non retryable messages");
        return StreamThreadExceptionResponse.SHUTDOWN_CLIENT;
    }
}
